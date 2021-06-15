package org.laeq.model.statistic


import org.laeq.StatisticService
import org.laeq.model.Video
import org.laeq.service.VideoGenerator
import spock.lang.Specification

class TarjanTest extends Specification {
    Video video1
    Video video2
    StatisticService service

    void setup() {
        video1 = VideoGenerator.generateVideo(1)
        video2 = VideoGenerator.generateVideo(1)

        VideoGenerator.generatePoints(video1, 1, 1, 20)
        VideoGenerator.generatePoints(video2, 1, 2, 10)

        service = new StatisticService();
    }

    def "GetSummaries"() {
        when:
        service.execute(Arrays.asList(video1, video2), 1)
        List<Tarjan> tarjans = service.getTarjanDiff();
        Result result = tarjans.get(0).getSummaryVideo1()
        Result result2 = tarjans.get(0).getSummaryVideo2()

        then:
        result.lonely == 10
        result.matched == 10
        result2.matched == 10
        result2.lonely == 0
    }

    def "get chart series"() {
        when:
        service.execute(Arrays.asList(video1, video2), 1)
        List<Tarjan> tarjans = service.getTarjanDiff();
        Tarjan result = tarjans.get(0);
        def serie_1 = result.getSerieVideo1(5)
        def serie_2 = result.getSerieVideo2(5)
        def serie_matched = result.getSerieMatched(5)

        def expected_1 = new HashMap()
        expected_1.put("5", 2);
        expected_1.put("10", 2);
        expected_1.put("15", 3);
        expected_1.put("20", 2);
        expected_1.put("25", 1);
        expected_1.put("30", 0);
        expected_1.put("35", 0);
        expected_1.put("40", 0);
        expected_1.put("45", 0);
        expected_1.put("50", 0);
        expected_1.put("55", 0);
        expected_1.put("60", 0);

        def matched_expected = new HashMap()
        matched_expected.put("5", 2);
        matched_expected.put("10", 3);
        matched_expected.put("15", 2);
        matched_expected.put("20", 3);
        matched_expected.put("25", 0);
        matched_expected.put("30", 0);
        matched_expected.put("35", 0);
        matched_expected.put("40", 0);
        matched_expected.put("45", 0);
        matched_expected.put("50", 0);
        matched_expected.put("55", 0);
        matched_expected.put("60", 0);

        def expected_2 = new HashMap()
        expected_2.put("5", 0);
        expected_2.put("10", 0);
        expected_2.put("15", 0);
        expected_2.put("20", 0);
        expected_2.put("25", 0);
        expected_2.put("30", 0);
        expected_2.put("35", 0);
        expected_2.put("40", 0);
        expected_2.put("45", 0);
        expected_2.put("50", 0);
        expected_2.put("55", 0);
        expected_2.put("60", 0);


        then:
        serie_1 == expected_1
        serie_2 == expected_2
        serie_matched == matched_expected
    }
}
