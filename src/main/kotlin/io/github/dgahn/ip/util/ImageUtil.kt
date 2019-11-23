package io.github.dgahn.ip.util

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.Metadata
import ij.ImagePlus
import ij.process.ImageStatistics
import io.github.dgahn.ip.profile.embedded.Statistics
import java.awt.Image
import java.io.File
import java.util.*
import javax.imageio.ImageIO


class ImageUtil {

    companion object {

        private fun getMetadata(file: File): Metadata? {
            return ImageMetadataReader.readMetadata(file)
        }

        fun getMetadataMap(file: File): Map<String, String>? {
            val metaMap: MutableMap<String, String> = HashMap()
            for (directory in getMetadata(file)!!.directories) {
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

        fun getStatistics(file: File): Statistics {
            val read = ImageIO.read(file)

            var rSum: Double = 0.0
            var gSum: Double = 0.0
            var bSum: Double = 0.0
            val statistics = Statistics()
            val width = read.width
            val height = read.height

            for (x in 0 until width) {
                for (y in 0 until height) {
                    val rgb = read.getRGB(x, y)
                    val r = rgb.shr(16) and 0xFF
                    val g = rgb.shr(8) and 0xFF
                    val b = rgb and 0xFF

                    statistics.rMax = r
                    statistics.rMin = r
                    statistics.gMax = g
                    statistics.gMin = g
                    statistics.bMax = b
                    statistics.bMin = b

                    rSum += r
                    gSum += g
                    bSum += b
                }
            }

            val pixelCount = width * height
            statistics.rAvg = kotlin.math.floor(rSum / pixelCount)
            statistics.bAvg = kotlin.math.floor(bSum / pixelCount)
            statistics.gAvg = kotlin.math.floor(gSum / pixelCount)

            return statistics
        }

    }

}
