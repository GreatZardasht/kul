package com.kul.transformer

import org.objectweb.asm.tree.ClassNode

abstract class Transformer {

    abstract fun run(node: ClassNode, key: String): ClassNode

}