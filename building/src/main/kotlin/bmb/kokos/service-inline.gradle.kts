package bmb.kokos

import bmb.kokos.asm.InlineServiceLoaderAction

tasks.named("remapJar") {
    doLast(InlineServiceLoaderAction)
}
