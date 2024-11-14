package com.iideprived.rain.implementation.jobs

import java.time.ZoneOffset

/**
 * Enum representing various timezones with their respective offsets and example cities/countries.
 */
enum class Timezone(val offset: String, val cities: String) {
    /**
     * Coordinated Universal Time
     * Offset: UTC+0
     * Cities/Countries: London (UK during winter), Reykjavik (Iceland), Accra (Ghana)
     */
    UTC("UTC+0", "London, Reykjavik, Accra"),

    /**
     * Pacific Standard Time
     * Offset: UTC-8
     * Cities/Countries: Los Angeles (USA), Vancouver (Canada)
     */
    PST("UTC-8", "Los Angeles, Vancouver"),

    /**
     * Eastern Standard Time
     * Offset: UTC-5
     * Cities/Countries: New York (USA), Toronto (Canada)
     */
    EST("UTC-5", "New York, Toronto"),

    /**
     * Central Standard Time
     * Offset: UTC-6
     * Cities/Countries: Chicago (USA), Mexico City (Mexico)
     */
    CST("UTC-6", "Chicago, Mexico City"),

    /**
     * Mountain Standard Time
     * Offset: UTC-7
     * Cities/Countries: Denver (USA), Calgary (Canada)
     */
    MST("UTC-7", "Denver, Calgary"),

    /**
     * Alaska Standard Time
     * Offset: UTC-9
     * Cities/Countries: Anchorage (USA)
     */
    AKST("UTC-9", "Anchorage"),

    /**
     * Hawaii Standard Time
     * Offset: UTC-10
     * Cities/Countries: Honolulu (USA)
     */
    HST("UTC-10", "Honolulu"),

    /**
     * Greenwich Mean Time
     * Offset: UTC+0
     * Cities/Countries: London (UK during winter), Dublin (Ireland)
     */
    GMT("UTC+0", "London, Dublin"),

    /**
     * Central European Time
     * Offset: UTC+1
     * Cities/Countries: Berlin (Germany), Paris (France)
     */
    CET("UTC+1", "Berlin, Paris"),

    /**
     * Eastern European Time
     * Offset: UTC+2
     * Cities/Countries: Athens (Greece), Helsinki (Finland)
     */
    EET("UTC+2", "Athens, Helsinki"),

    /**
     * Japan Standard Time
     * Offset: UTC+9
     * Cities/Countries: Tokyo (Japan), Osaka (Japan)
     */
    JST("UTC+9", "Tokyo, Osaka"),

    /**
     * Korea Standard Time
     * Offset: UTC+9
     * Cities/Countries: Seoul (South Korea)
     */
    KST("UTC+9", "Seoul"),

    /**
     * Australian Eastern Standard Time
     * Offset: UTC+10
     * Cities/Countries: Sydney (Australia), Melbourne (Australia)
     */
    AEST("UTC+10", "Sydney, Melbourne"),

    /**
     * New Zealand Standard Time
     * Offset: UTC+12
     * Cities/Countries: Wellington (New Zealand), Auckland (New Zealand)
     */
    NZST("UTC+12", "Wellington, Auckland");


    /**
     * Converts the timezone offset to a ZoneOffset object.
     * @return ZoneOffset corresponding to the timezone offset.
     */
    fun toZonedOffset(): ZoneOffset {
        return ZoneOffset.of(offset.replace("UTC", ""))
    }
}