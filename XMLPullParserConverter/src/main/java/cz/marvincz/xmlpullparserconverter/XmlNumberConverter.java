package cz.marvincz.xmlpullparserconverter;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.apache.commons.lang3.reflect.Typed;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

public abstract class XmlNumberConverter<T> extends AbstractXmlStringConverter<T> {
    private final Class<T> clazz;

    public XmlNumberConverter(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Typed<T> getType() {
        return TypeUtils.wrap(clazz);
    }

    static class IntConverter extends XmlNumberConverter<Integer> {
        IntConverter() {
            super(Integer.class);
        }

        @Nullable
        @Override
        protected Integer convertString(@Nullable String stringValue) {
            return Integer.valueOf(stringValue);
        }
    }

    static class ShortConverter extends XmlNumberConverter<Short> {
        ShortConverter() {
            super(Short.class);
        }

        @Nullable
        @Override
        protected Short convertString(@Nullable String stringValue) {
            return Short.valueOf(stringValue);
        }
    }

    static class ByteConverter extends XmlNumberConverter<Byte> {
        ByteConverter() {
            super(Byte.class);
        }

        @Nullable
        @Override
        protected Byte convertString(@Nullable String stringValue) {
            return Byte.valueOf(stringValue);
        }
    }

    static class FloatConverter extends XmlNumberConverter<Float> {
        FloatConverter() {
            super(Float.class);
        }

        @Nullable
        @Override
        protected Float convertString(@Nullable String stringValue) {
            return Float.valueOf(stringValue);
        }
    }

    static class DoubleConverter extends XmlNumberConverter<Double> {
        DoubleConverter() {
            super(Double.class);
        }

        @Nullable
        @Override
        protected Double convertString(@Nullable String stringValue) {
            return Double.valueOf(stringValue);
        }
    }

    static class BigDecimalConverter extends XmlNumberConverter<BigDecimal> {
        BigDecimalConverter() {
            super(BigDecimal.class);
        }

        @Nullable
        @Override
        protected BigDecimal convertString(@Nullable String stringValue) {
            return new BigDecimal(stringValue);
        }
    }

    static class BigIntegerConverter extends XmlNumberConverter<BigInteger> {
        BigIntegerConverter() {
            super(BigInteger.class);
        }

        @Nullable
        @Override
        protected BigInteger convertString(@Nullable String stringValue) {
            return new BigInteger(stringValue);
        }
    }
}
