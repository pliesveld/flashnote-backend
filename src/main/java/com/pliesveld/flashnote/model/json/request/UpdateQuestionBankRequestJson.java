package com.pliesveld.flashnote.model.json.request;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
public class UpdateQuestionBankRequestJson extends ModelBase implements JsonWebRequestSerializable {
    private static final long serialVersionUID = 7555833296567415969L;

    public enum UpdateOperation {
        ADD_QUESTION,
        REMOVE_QUESTION
    }

    @NotNull
    UpdateOperation operation;

    @NotNull
    @Valid
    Question question;

    public UpdateQuestionBankRequestJson() {
    }

    public void setOperation(UpdateOperation operation) {
        this.operation = operation;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public UpdateOperation getOperation() {
        return operation;
    }

    public Question getQuestion() {
        return question;
    }
}
