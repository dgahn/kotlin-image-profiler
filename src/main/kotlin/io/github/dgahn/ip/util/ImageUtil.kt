package io.github.dgahn.ip.util

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import ij.ImagePlus
import ij.process.ImageStatistics
import java.awt.Image
import java.io.File
import java.util.HashMap
import javax.imageio.ImageIO


class ImageUtil {

    companion object {

        private fun getMetadata(file: File): Metadata {
            return ImageMetadataReader.readMetadata(file)
        }

        fun getMetadataMap(file: File): Map<String, String>? {
            val metaMap: MutableMap<String, String> = HashMap()
            for (directory in getMetadata(file).directories) {
                for (tag in directory.tags) {
                    metaMap[tag.tagName] = tag.description
                }
            }
            return metaMap
        }

        fun getImageStatistics(file: File): ImageStatistics? {
            val read: Image
            read = ImageIO.read(file)
            val imp = ImagePlus()
            imp.image = read
            val ip = imp.processor
            return ImageStatistics.getStatistics(ip)
        }

    }

}
