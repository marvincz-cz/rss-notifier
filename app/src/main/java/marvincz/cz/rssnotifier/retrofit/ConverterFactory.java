package marvincz.cz.rssnotifier.retrofit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import marvincz.cz.rssnotifier.model.Rss;
import marvincz.cz.rssnotifier.xml.XmlConverterFactory;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class ConverterFactory extends Converter.Factory {
    public static ConverterFactory INSTANCE = new ConverterFactory();

    private ConverterFactory() {}

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type.equals(Rss.class)) {
            return RssConverter.INSTANCE;
        }
        return null;
    }

    private static class RssConverter implements Converter<ResponseBody, Rss> {
        static final RssConverter INSTANCE = new RssConverter();

        @Override
        public Rss convert(@NonNull ResponseBody value) throws IOException {
            try (InputStream in = value.byteStream()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return XmlConverterFactory.convert(Rss.class, parser, "rss", null);
            } catch (XmlPullParserException e) {
                throw new IOException(e);
            }
        }
    }
}
