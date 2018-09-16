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
            return RssChannelConverter.INSTANCE;
        }
        return null;
    }

    private static class RssChannelConverter implements Converter<ResponseBody, Rss> {
        static final RssChannelConverter INSTANCE = new RssChannelConverter();

        @Override
        public Rss convert(@NonNull ResponseBody value) throws IOException {
            try (InputStream in = value.byteStream()) {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return new Rss.Parser().parseTag(parser, "rss", null);
            } catch (XmlPullParserException e) {
                throw new IOException(e);
            }
        }
    }
}
