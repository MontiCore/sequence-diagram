package de.monticore.lang;

import de.monticore.lang.sd4development._parser.SD4DevelopmentParser;
import de.monticore.lang.sd4development.prettyprint.PrettyPrinter;
import de.monticore.lang.sdbasis._ast.ASTSDArtifact;
import de.monticore.prettyprint.IndentPrinter;
import de.se_rwth.commons.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrettyPrinterTest  {
    private final static String CORRECT_PATH = "src/test/resources/examples/";
    private SD4DevelopmentParser parser;

    @BeforeEach
    void setup() {
        this.parser = new SD4DevelopmentParser();
        Log.enableFailQuick(false);
    }
    String readFile(String path, String model) {
        try {
            return new String(Files.readAllBytes(Paths.get(path + model)));
        }
        catch (IOException e ) {
            System.err.println("Read model  " + model + " failed: " + e.getMessage());
            fail();
            return null;
        }
    }

    @ParameterizedTest
    @CsvSource({
           "example_pretty.sd"
    })
    void testCorrectExamples(String model) {
        ASTSDArtifact parsed = testParseModel(CORRECT_PATH, model);
        if(parsed != null) {
            PrettyPrinter pp = new PrettyPrinter(new IndentPrinter());
            String printed = pp.prettyprint(parsed);
            String expected = readFile(CORRECT_PATH, model);
            String actual = pp.getResult();
            assertEquals(expected, printed);
        }
    }
    private ASTSDArtifact testParseModel(String path, String model) {
        try {
            Optional<ASTSDArtifact> ast = parser.parse(path + model);
            return ast.orElse(null);
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Loading model: " + model + " failed: " + e.getMessage());
            fail();
        }
        return null;
    }
}
