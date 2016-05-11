package com.pliesveld.flashnote.serializer.module;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;

/**
 * Custom serializer to include fields of Spring Data Page objects
 * when using @JsonView.
 */
public class SpringDataPageSerializer extends BeanSerializer
{
    private static final Logger LOG = LogManager.getLogger();

    public SpringDataPageSerializer(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
        super(type, builder, properties, filteredProperties);
    }

    protected SpringDataPageSerializer(BeanSerializerBase src) {
        super(src);
    }

    protected SpringDataPageSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
        super(src, objectIdWriter);
    }

    protected SpringDataPageSerializer(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
        super(src, objectIdWriter, filterId);
    }

    protected SpringDataPageSerializer(BeanSerializerBase src, String[] toIgnore) {
        super(src, toIgnore);
    }

    @Override
    protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
        final BeanPropertyWriter[] props;

        if (bean instanceof Page || bean instanceof Sort.Order) {
//            LOG.debug(Markers.OBJECT_MAPPER_WRITE, "Skipping {}", () -> bean);
            // for Page DO NOT filter anything so that @JsonView is passthrough at this level
            props = _props;
        } else {

            if (_filteredProps != null && provider.getActiveView() != null) {
                props = _filteredProps;
            } else {
                props = _props;
            }

        }

        int i = 0;
        try {
            for (final int len = props.length; i < len; ++i) {
                BeanPropertyWriter prop = props[i];
                if (prop != null) { // can have nulls in filtered list
                    prop.serializeAsField(bean, gen, provider);
                }
            }
            if (_anyGetterWriter != null) {
                _anyGetterWriter.getAndSerialize(bean, gen, provider);
            }
        } catch (Exception e) {
            String name = (i == props.length) ? "[anySetter]" : props[i].getName();
            wrapAndThrow(provider, e, bean, name);
        } catch (StackOverflowError e) {
            /* 04-Sep-2009, tatu: Dealing with this is tricky, since we do not
             *   have many stack frames to spare... just one or two; can't
             *   make many calls.
             */
            JsonMappingException mapE = new JsonMappingException("Infinite recursion (StackOverflowError)", e);
            String name = (i == props.length) ? "[anySetter]" : props[i].getName();
            mapE.prependPath(new JsonMappingException.Reference(bean, name));
            throw mapE;
        }

    }
}
