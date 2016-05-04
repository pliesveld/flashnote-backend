package com.pliesveld.flashnote.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QuestionSetDeserializer extends JsonDeserializer<Question> {

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public Question deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        return questionRepository.getOne(node.asInt());
//        return questionRepository.findOne(node.asInt());
//        Assert.isTrue(node.isArray(),"Parser should be array");
//
//        ArrayList<Integer> question_ids = new ArrayList<>(node.size());
//
//        node.iterator().forEachRemaining((ele) ->
//        {
//            Integer id = ele.asInt();
//            question_ids.add(id);
//        });
//
//        Set<Question> questions = new HashSet<Question>();
//        questionRepository.findAll(question_ids).forEach(questions::add);
//        return questions;

    }
}
