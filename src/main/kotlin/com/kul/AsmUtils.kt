package com.kul

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import java.util.zip.ZipFile


object AsmUtils {

    private val classNodes: MutableMap<String, ClassNode> = HashMap()

    fun loadFile(file: File) {

        // Check if its a jar file
        if(file.extension == "jar" || file.extension == "zip") {

            ZipFile(file).use {

                for(entry in it.entries()) {

                    // Remove / at the end (only applies to packages)
                    val name = entry.name.removeSuffix("/")

                    // Read file
                    val bytes = it.getInputStream(entry).readBytes()

                    // If its a .class file and its not empty
                    if(name.endsWith(".class") && entry.size > 1) {

                        val c = ClassNode()
                        try {
                            ClassReader(bytes).accept(c, ClassReader.EXPAND_FRAMES)
                            classNodes[c.name] = c
                        } catch(ignored: Exception) {}

                    }

                }

            }

        }

    }

    fun saveJar(output: String) {
        var loc = output
        if (!loc.endsWith(".jar")) loc += ".jar"
        val jarPath = Paths.get(loc)
        Files.deleteIfExists(jarPath)
        val outJar = JarOutputStream(Files.newOutputStream(jarPath, *arrayOf(StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)))
        //Write classes into obf jar
        for (node in getClassNodes().values) {
            val entry = JarEntry(node.name + ".class")
            outJar.putNextEntry(entry)
            val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
            node.accept(writer)
            outJar.write(writer.toByteArray())
            outJar.closeEntry()
        }
        outJar.close()
    }

    fun getClassNodes(): MutableMap<String, ClassNode> {
        return classNodes
    }


}