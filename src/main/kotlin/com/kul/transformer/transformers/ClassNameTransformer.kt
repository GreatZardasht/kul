package com.kul.transformer.transformers

import com.kul.transformer.Transformer
import org.objectweb.asm.tree.ClassNode

class ClassNameTransformer : Transformer() {

    override fun run(key: String, node: ClassNode): ClassNode {

        val charPool : CharArray = "鮉縑᱘晜骫炀嶾额諕臝箙䵆�뾛腻㛞崙购袋㿦塚蓻닄箙䵆".toCharArray()

        val randomString = (1..4)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");

        println("Renamed class " + node.name)
        node.name = randomString
        println("   -> " + node.name)

        return node

    }

}