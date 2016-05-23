package com.segmenter.test;

import com.segmenter.application.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:8080")
public class SegmenterProduceTest {

	@Autowired
	private Segmenter segmenter;

    private SegmenterSettings confg;
	
	/************************************************ Test Data *************************************************/

    private static final String LINE_STR = "HG09B7H04ERWXT16C87H client=client1&timestamp=2446";
    private static final Line LINE = new Line("HG09B7H04ERWXT16C87H", "client1", 2446);


    private static final Line LINE1 = new Line("E9TVGJVTMODNG9GRKP0X", "client2", 9409);
    private static final Line LINE2 = new Line("8ARV4Q0AEXG2MLK7L20K", "client1", 5154);
    private static final Line LINE3 = new Line("EL194VSJKG6Y5CWXP2QW", "client0", 2918);

    private static final HashMap<String, SortedSet> MAP1 = new HashMap<>();
    static
    {
        MAP1.put(LINE1.getClient(), new TreeSet<>(Arrays.asList(LINE1)));
        MAP1.put(LINE2.getClient(), new TreeSet<>(Arrays.asList(LINE2)));
        MAP1.put(LINE3.getClient(), new TreeSet<>(Arrays.asList(LINE3)));
    }

    private static final Line LINE4 = new Line("2IFVL36UQZ8PCM1WA3LV", "client3", 8707);
    private static final Line LINE5 = new Line("YW5EUQ6XV2B2Q1KQL0SL", "client1", 266);
    private static final Line LINE6 = new Line("R5MC5C6ELR483ARSYA54", "client2", 988);
    private static final Line LINE7 = new Line("ZR5MC5C6ELR483TITRUR", "client2", 6840);

    private static final HashMap<String, SortedSet> MAP2 = new HashMap<>();
    static
    {
        MAP2.put("client0", new TreeSet<>(Arrays.asList(LINE3)));
        MAP2.put("client1", new TreeSet<>(Arrays.asList(LINE2, LINE5)));
        MAP2.put("client2", new TreeSet<>(Arrays.asList(LINE1, LINE6, LINE7)));
        MAP2.put("client3", new TreeSet<>(Arrays.asList(LINE4)));
    }
    private static final ArrayList<Line> ORDERED_LIST = new ArrayList<>(Arrays.asList(LINE6, LINE7, LINE1));

	/*************************************************************************************************************/
	
	@Before
	public void setUp() {
		this.confg = segmenter.getConfig();
	}


    /**
     *  Test Line bean creation by parsing a string of the correct format
     *  comparing it with the test bean LINE
     */
    @Test
    public void testBaseUrlValidity() {

        Assert.assertTrue(SegmenterSettings.isBaseUrlValid("https://www.alephd.com/word/"));

        try {
            SegmenterSettings.isBaseUrlValid("https://www.alephd.com/");
            Assert.fail( "Base url should not be valid!" );
        } catch (UnsupportedOperationException e) {
        }

        try {
            SegmenterSettings.isBaseUrlValid("");
            Assert.fail( "Base url should not be valid!" );
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     *  Test Line bean creation by parsing a string of the correct format
     *  comparing it with the test bean LINE
     */
	@Test
	public void testLineBuild() {

		Assert.assertEquals(LINE, new Line(LINE_STR, confg));
	}

    /**
     * Test bean creation failure for empty string and wrong string format
     */
    @Test
    public void testLineBuildFail() {

        // Test bean creation failure for empty string
        try {
            Line l = new Line("", confg);
            Assert.assertTrue(false);
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }

        // Test bean creation failure for wrong string
        try {
            Line l = new Line("HG09B7H04ERWXT16C87H_client:client1&timestamp:2446", confg);
            Assert.assertTrue(false);
        } catch (UnsupportedOperationException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Testing Producer thread requesting http://localhost:8080/logs/test0.log
     * Parsing the content of this log should give an HashMap equal to test data MAP1
     */
    @Test
    public void testProducerThread() {
        final int n = 0;

        segmenter.resetMap(); // To be sure about the test consistence

        String url = confg.getBaseUrl()+confg.getLogname(n);
        Producer p = new Producer(url, n, confg);
        p.run();

        Assert.assertEquals(MAP1, segmenter.getMap());
    }

    /**
     * Test the Segmenter class running for the first two logs (test0.log, test1.log)
     * Each thread parsing the content of these logs concurrently should give an HashMap equal to test data MAP2
     */
	@Test
	public void testSegmentLines() {
		final String baseUrl = confg.getBaseUrl();
        final int filesNumber = 2;

        segmenter.resetMap(); // To be sure about the test consistence

		segmenter.run(baseUrl, filesNumber);

        Assert.assertEquals(MAP2, segmenter.getMap());


        // Test order of the most numerous TreeSet in the map (client2).
        // Unnecessary since TreeSet keep order on Line timestamp
        SortedSet<Line> client2 = segmenter.getMap().get("client2");
        Assert.assertEquals(ORDERED_LIST, new ArrayList<>(client2));
	}

}