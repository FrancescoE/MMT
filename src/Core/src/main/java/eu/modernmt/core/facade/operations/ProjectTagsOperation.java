package eu.modernmt.core.facade.operations;

import eu.modernmt.aligner.Aligner;
import eu.modernmt.aligner.AlignerException;
import eu.modernmt.aligner.SymmetrizedAligner;
import eu.modernmt.aligner.symal.SymmetrizationStrategy;
import eu.modernmt.core.Engine;
import eu.modernmt.model.Sentence;
import eu.modernmt.model.Translation;
import eu.modernmt.processing.Preprocessor;
import eu.modernmt.processing.framework.LanguageNotSupportedException;
import eu.modernmt.processing.framework.ProcessingException;
import eu.modernmt.processing.xml.XMLTagProjector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by davide on 22/04/16.
 */
public class ProjectTagsOperation extends Operation<Translation> {

    private static final Logger logger = LogManager.getLogger(ProjectTagsOperation.class);
    private static final XMLTagProjector tagProjector;

    static {
        try {
            tagProjector = new XMLTagProjector();
        } catch (LanguageNotSupportedException e) {
            throw new Error("This cannot happen", e);
        }
    }

    private static Preprocessor targetPreprocessor = null;

    private String sentenceString;
    private String translationString;
    private final SymmetrizationStrategy strategy;
    private final boolean inverted;

    public ProjectTagsOperation(String sentence, String translation, boolean inverted, SymmetrizationStrategy strategy) {
        this.sentenceString = sentence;
        this.translationString = translation;
        this.strategy = strategy;
        this.inverted = inverted;
    }

    @Override
    public Translation call() throws ProcessingException, AlignerException {
        Engine engine = getEngine();
        Aligner aligner = engine.getAligner();
        Preprocessor preprocessor = engine.getPreprocessor();

        if (targetPreprocessor == null) {
            synchronized (ProjectTagsOperation.class) {
                if (targetPreprocessor == null) {
                    targetPreprocessor = new Preprocessor(engine.getTargetLanguage());
                }
            }
        }

        long beginTime = System.currentTimeMillis();
        long endTime;

        String sentenceString = this.inverted ? this.translationString : this.sentenceString;
        String translationString = this.inverted ? this.sentenceString : this.translationString;

        Sentence sentence = preprocessor.process(sentenceString, true);
        Sentence translation = targetPreprocessor.process(translationString, true);

        if (this.strategy != null) {
            if (aligner instanceof SymmetrizedAligner)
                ((SymmetrizedAligner) aligner).setSymmetrizationStrategy(this.strategy);
            else
                throw new AlignerException("Symmetrization strategy specified but aligner is not an instance of " +
                        "SymmetrizedAligner: " + aligner.getClass());
        }

        int[][] alignments = aligner.getAlignments(sentence, translation);

        if (this.inverted) {
            Aligner.invertAlignments(alignments);
            Sentence tmp = sentence;
            sentence = translation;
            translation = tmp;
        }

        Translation taggedTranslation = new Translation(translation.getWords(), sentence, alignments);
        tagProjector.call(taggedTranslation, null);

        endTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("Total time for tags projection: " + (endTime - beginTime) + " [ms]");
        }

        return taggedTranslation;
    }

}
