package com.example.demo.validation;

import com.example.demo.form.AnimeForm;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class AnimeFormValidationTest {
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void NameがNullの場合バリデーションエラーになること() {
        AnimeForm animeForm = new AnimeForm();
        animeForm.setName(null);
        animeForm.setGenre("romance");

        Set<ConstraintViolation<AnimeForm>> constraintViolations = validator.validate(animeForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("name", "cannot be empty")
                );
    }

    @Test
    public void GenreがNullの場合バリデーションエラーになること() {
        AnimeForm animeForm = new AnimeForm();
        animeForm.setName("Your Name");
        animeForm.setGenre(null);

        Set<ConstraintViolation<AnimeForm>> constraintViolations = validator.validate(animeForm);

        assertThat(constraintViolations.size()).isEqualTo(1);
        assertThat(constraintViolations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("genre", "cannot be empty")
                );
    }

    @Test
    public void すべてのフィールドがNullの場合バリデーションエラーになること() {
        AnimeForm animeForm = new AnimeForm();
        animeForm.setName(null);
        animeForm.setGenre(null);

        Set<ConstraintViolation<AnimeForm>> constraintViolations = validator.validate(animeForm);

        assertThat(constraintViolations.size()).isEqualTo(2);
        assertThat(constraintViolations)
                .extracting(
                        propertyPath -> propertyPath.getPropertyPath().toString(),
                        message -> message.getMessage())
                .containsOnly(
                        tuple("name", "cannot be empty"),
                        tuple("genre", "cannot be empty")
                );
    }

    @Test
    public void 正式なフィールドの場合バリデーションエラーにならないこと() {
        AnimeForm animeForm = new AnimeForm();
        animeForm.setName("Your Name");
        animeForm.setGenre("Romance");

        Set<ConstraintViolation<AnimeForm>> constraintViolations = validator.validate(animeForm);

        assertThat(constraintViolations.size()).isEqualTo(0);
    }
}
