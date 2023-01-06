@file:Suppress("UNCHECKED_CAST")

package bmb.kokos.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.MethodNode
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption

interface MatchContext {
    val instructions: List<AbstractInsnNode>
    fun replaceWith(instructions: List<AbstractInsnNode>)
}

data class InsnPattern<T : AbstractInsnNode>(val type: Class<T>, val filter: (T) -> Boolean)
data class MethodBodyPattern(val patterns: List<InsnPattern<*>>) {
    fun match(node: MethodNode, visitor: (MatchContext) -> Unit): Boolean {
        for (insn in node.instructions) {
            val start = patterns.first() as InsnPattern<AbstractInsnNode>
            if (start.type.isInstance(insn) && start.filter(insn)) {
                val insnSequence = sequence<AbstractInsnNode> {
                    var current = insn.next
                    while (current != null) {
                        yield(current)
                        current = current.next
                    }
                }.filter { it !is LabelNode && it !is LineNumberNode && !(it is InsnNode && it.opcode == Opcodes.NOP) }
                val matches = patterns.asSequence().drop(1).zip(insnSequence).filter { (pattern, node) ->
                    pattern.type.isInstance(node) && (pattern as InsnPattern<AbstractInsnNode>).filter(node)
                }.map { (_, node) -> node }.toList()
                if (matches.size >= patterns.size - 1) continue
                val matchedInstructions = listOf(insn) + matches
                val context = object : MatchContext {
                    override val instructions: List<AbstractInsnNode> = matchedInstructions
                    override fun replaceWith(instructions: List<AbstractInsnNode>) {
                        require(instructions.size >= matchedInstructions.size) { "Incorrect number of replacements" }
                        for (i in matchedInstructions.indices) node.instructions.set(matchedInstructions[i], instructions[i])
                        if (instructions.size > matchedInstructions.size) for (i in matchedInstructions.size..instructions.lastIndex)
                            node.instructions.insert(instructions[i - 1], instructions[i])
                    }
                }
                visitor(context)
                return true
            }
        }
        return false
    }
}

fun transformJar(jar: Path, transformer: (filer: (String) -> Path, ClassNode) -> Unit) {
    val jarUri = URI.create("jar:${jar.toUri()}")
    FileSystems.newFileSystem(jarUri, mapOf("create" to false)).use { fs ->
        for (root in fs.rootDirectories) Files.walk(root).filter {
            Files.isRegularFile(it) && it.fileName.toString().endsWith(".class")
        }.use { classes ->
            for (clazz in classes) {
                // Read the existing class
                val cr = Files.newInputStream(clazz).use { ClassReader(it) }
                val node = ClassNode()
                cr.accept(node, 0)
                // Transform
                transformer(fs::getPath, node)
                // Write out the new class
                val cw = ClassWriter(cr, 0)
                node.accept(cw)
                Files.write(clazz, cw.toByteArray(), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)
            }
        }
    }
}
