import com.opencsv.CSVReader
import org.sil.storyproducer.model.WordLink
import java.io.Reader

class WordLinkCSVReader(reader: Reader): AutoCloseable {
    private val csvReader = CSVReader(reader)

    init {
        val numberOfHeaderRows = 1
        csvReader.skip(numberOfHeaderRows)
    }

    fun readAll(): List<WordLink>{
        val wordLinks: MutableList<WordLink> = mutableListOf()

        val lines = csvReader.readAll()
        for (line in lines) {
            val wordLink = lineToWordLink(line)
            wordLinks.add(wordLink)
        }
        wordLinks.removeAll{ wordlink -> wordlink.term == ""}

        return wordLinks
    }

    override fun close() {
        csvReader.close()
    }

    private fun lineToWordLink(line: Array<String>): WordLink {
        val wordLink = WordLink()

        wordLink.term = line[1].trim()
        wordLink.termForms = stringToList(line[2], ",")
        wordLink.alternateRenderings = stringToList(line[3], ";")
        wordLink.explanation = line[4].trim()
        wordLink.relatedTerms = stringToList(line[5], ",")

        return wordLink
    }

    /*
     * The purpose of this method is to split a string based on a give separator.
     */
    private fun stringToList(field: String, separator: String): List<String>{
        if(field.isNotEmpty()) {
            val list = field.split(separator)
            val trimmedList = list.asSequence().map { it.trim() }.toMutableList()
            //Trim any empty string elements
            trimmedList.removeAll { it == "" }
            return trimmedList
        }
        else{
            return listOf()
        }
    }
}