package org.laeq

import spock.lang.Specification

class TranlationServiceTest extends Specification {
    File file = new File("src/test/resources/messages.json")

    def "unmarchall french."() {
        setup:
        TranslationService service = new TranslationService(file, Locale.CANADA_FRENCH)

        when:
        def result = service.getMessage("org.laeq.menu.tooltip.video_list")

        then:

        result == "Ouvrir la section vidéo"
    }

    def "unmarchall english."() {
        setup:
        TranslationService service = new TranslationService(file, new Locale("en", "CA"))

        when:
        def result = service.getMessage("org.laeq.menu.tooltip.video_add")

        then:

        result == "Add a new video"
    }

    def "unmarchall missing key."() {
        setup:
        TranslationService service = new TranslationService(file, new Locale("en", "CA"))

        when:
        def result = service.getMessage("mock.key.missing")

        then:

        result == "Missing mock.key.missing for en_CA"
    }

    def "unmarchall language."() {
        setup:
        TranslationService service = new TranslationService(file, new Locale("en", "UK"))

        when:
        def result = service.getMessage("org.laeq.menu.tooltip.video_add")

        then:

        result == "Missing org.laeq.menu.tooltip.video_add for en_UK"
    }
}
