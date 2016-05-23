package com.segmenter.test;

import com.segmenter.application.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
public class SegmenterConsumeTest {

	@Autowired
	private Segmenter segmenter;

    private SegmenterSettings confg;
	
	/************************************************ Test Data *************************************************/
    private static final Line LINE1 = new Line("E9TVGJVTMODNG9GRKP0X", "client1", 1234);
    private static final Line LINE2 = new Line("8ARV4Q0AEXG2MLK7L20K", "client1", 4567);
    private static final Line LINE3 = new Line("EL194VSJKG6Y5CWXP2QW", "client1", 7891);
    private static final TreeSet<Line> SET = new TreeSet<>(Arrays.asList(LINE3,LINE1,LINE2));

    private static final Consumer CONSUMER = new Consumer(SET);
    /*************************************************************************************************************/
	
	@Before
	public void setUp() {
		this.confg = segmenter.getConfig();
	}


    /**
     * Write a Consumer with the test data SET
     * and confront the SET with what is written in the file
     */
    @Test
    public void testConsumer() {
        final String fileName = "test";

        CONSUMER.write(fileName);

        List<Line> list = new ArrayList<>();

        // Read written file and put each line in List<Line>
        try (Stream<String> stream = Files.lines(Paths.get(fileName+CONSUMER.EXT))) {
            list = stream
                    .map(s -> new Line(s, confg))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Files.deleteIfExists(Paths.get(fileName));
            } catch (IOException e) {}
        }

        Assert.assertEquals(new ArrayList<>(SET), list);
    }

}