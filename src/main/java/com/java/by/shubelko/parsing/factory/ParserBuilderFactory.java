package com.java.by.shubelko.parsing.factory;

import com.java.by.shubelko.parsing.exception.MedProductException;
import com.java.by.shubelko.parsing.builder.AbstractMedProductBuilder;
import com.java.by.shubelko.parsing.builder.impl.MedProductDomBuilder;
import com.java.by.shubelko.parsing.builder.impl.MedProductSaxBuilder;
import com.java.by.shubelko.parsing.builder.impl.MedProductStaxBuilder;

public class ParserBuilderFactory {

    private ParserBuilderFactory() {
    }

    public enum TypeParser {
        DOM,
        StAX,
        SAX
    }

    public static AbstractMedProductBuilder newBuilder(TypeParser type) throws MedProductException {
        return switch (type) {
            case SAX -> new MedProductSaxBuilder();
            case StAX -> new MedProductStaxBuilder();
            case DOM -> new MedProductDomBuilder();
            default -> throw new EnumConstantNotPresentException(
                    type.getDeclaringClass(), type.name());
        };
    }
}