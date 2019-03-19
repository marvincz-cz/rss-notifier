package cz.marvincz.rssnotifier.xml

import cz.marvincz.xmlpullparserconverter.AbstractXmlStringConverter
import org.apache.commons.lang3.reflect.TypeLiteral
import org.apache.commons.lang3.reflect.Typed
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class ABPZonedDateTimeConverter : AbstractXmlStringConverter<ZonedDateTime>() {

    override fun getType(): Typed<ZonedDateTime> {
        return object : TypeLiteral<ZonedDateTime>() {

        }
    }

    override fun convertString(stringValue: String?): ZonedDateTime? {
        return if (stringValue != null) {
            ZonedDateTime.from(formatter.parse(stringValue))
        } else {
            null
        }
    }

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm:ss z", Locale.US)
    }
}
