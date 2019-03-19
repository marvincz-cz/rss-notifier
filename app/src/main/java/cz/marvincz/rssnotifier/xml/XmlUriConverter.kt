package cz.marvincz.rssnotifier.xml

import android.net.Uri

import org.apache.commons.lang3.reflect.TypeLiteral
import org.apache.commons.lang3.reflect.Typed
import cz.marvincz.xmlpullparserconverter.AbstractXmlStringConverter

class XmlUriConverter : AbstractXmlStringConverter<Uri>() {
    override fun getType() = object : TypeLiteral<Uri>() {}

    override fun convertString(stringValue: String?): Uri? {
        return if (stringValue != null) {
            Uri.parse(stringValue)
        } else null
    }
}
