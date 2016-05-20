package eu.modernmt.core.facade;

import eu.modernmt.aligner.AlignerException;
import eu.modernmt.aligner.symal.Symmetrization;
import eu.modernmt.core.Engine;
import eu.modernmt.core.cluster.error.SystemShutdownException;
import eu.modernmt.core.facade.error.LanguageNotSupportedException;
import eu.modernmt.core.facade.error.TranslationException;
import eu.modernmt.core.facade.operations.ProjectTagsOperation;
import eu.modernmt.model.Translation;
import eu.modernmt.processing.Languages;
import eu.modernmt.processing.framework.ProcessingException;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by davide on 20/04/16.
 */
public class TagFacade {

    public Translation project(String sentence, String translation, Locale sourceLanguage, Locale targetLanguage)
            throws TranslationException {
        return project(sentence, translation, sourceLanguage, targetLanguage, null, null);
    }

    public Translation project(String sentence, String translation, Locale sourceLanguage, Locale targetLanguage, String hint)
            throws TranslationException {
        return project(sentence, translation, sourceLanguage, targetLanguage, hint, null);
    }

    public Translation project(String sentence, String translation, Locale sourceLanguage, Locale targetLanguage,
                               Symmetrization.Strategy symmetrizationStrategy) throws TranslationException {
        return project(sentence, translation, sourceLanguage, targetLanguage, null, symmetrizationStrategy);
    }

    public Translation project(String sentence, String translation, Locale sourceLanguage, Locale targetLanguage, String hint,
                               Symmetrization.Strategy symmetrizationStrategy) throws TranslationException {
        boolean inverted = isLanguagesInverted(sourceLanguage, targetLanguage, ModernMT.node.getEngine());
        ProjectTagsOperation operation = new ProjectTagsOperation(sentence, translation, inverted, hint, symmetrizationStrategy);
        try {
            return ModernMT.node.submit(operation).get();
        } catch (InterruptedException e) {
            throw new SystemShutdownException();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();

            if (cause instanceof ProcessingException)
                throw new TranslationException("Problem while processing translation", cause);
            else if (cause instanceof AlignerException)
                throw new TranslationException("Problem while computing alignments", cause);
            else if (cause instanceof RuntimeException)
                throw new TranslationException("Unexpected error while projecting tags", cause);
            else
                throw new Error("Unexpected exception: " + cause.getMessage(), cause);
        }
    }

    private static boolean isLanguagesInverted(Locale sourceLanguage, Locale targetLanguage, Engine engine)
            throws LanguageNotSupportedException {
        if (Languages.sameLanguage(engine.getSourceLanguage(), sourceLanguage) &&
                Languages.sameLanguage(engine.getTargetLanguage(), targetLanguage)) {
            return false;
        } else if (Languages.sameLanguage(engine.getSourceLanguage(), targetLanguage) &&
                Languages.sameLanguage(engine.getTargetLanguage(), sourceLanguage)) {
            return true;
        }
        throw new LanguageNotSupportedException(sourceLanguage, targetLanguage);
    }

}
