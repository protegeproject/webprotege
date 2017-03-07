package edu.stanford.bmir.protege.web.server.notes;

import org.semanticweb.owlapi.model.IRI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/04/2012
 * <p>
 *     An enum for describing star ratings (used in votes for change proposals).  The value of a star rating is
 *     defined by the ordinal value of each element in the enum.  The first element indicates a star rating of zero,
 *     the second element a star rating of 1 and so on.  Each star rating has a local name, which is used to construct
 *     an {@link IRI} for it.
 * </p>
 */
@Deprecated
public enum StarRating {

    ZERO_STAR("ZeroStar"),
    
    ONE_STAR("OneStar"),

    TWO_STAR("TwoStar"),

    THREE_STAR("ThreeStar"),

    FOUR_STAR("FourStar"),

    FIVE_STAR("FiveStar");
    
    
    public static final String PREFIX = "http://protege.stanford.edu/vocabulary/starrating#";


    /**
     * The Unicode Star Character.  This is used for computing a display value for a star rating.
     */
    public static final String STAR_CHARACTER = "\u2605";

    /**
     * A regular expression for the permissible string representations of StarRatings.  The pattern consists of a sequence
     * of stars (either Unicode Stars as specified by {@link #STAR_CHARACTER}, or plain asterisks).  The sequence may be
     * empty (i.e. the empty string), or may be a maximum length of the size of the StarRating enum minus 1.
     */
    public static final Pattern STAR_RATING_PATTERN = Pattern.compile("\\s*((\\*|" + Pattern.quote(STAR_CHARACTER) + "){0," + (StarRating.values().length - 1) + "})\\s*");

    public static final int STAR_RATING_PATTERN_STARS_GROUP = 1;


    private String localName;
    
    private IRI iri;
    
    private int value;
    
    private String displayName;

    /**
     * Constructs a star rating
     * @param localName The local name of the star rating.  This must not be null, must not be empty, and must be a
     * valid IRI fragment or path element.
     */
    private StarRating(String localName) {
        if(localName == null) {
            throw new NullPointerException("localName must not be null");
        }
        if(localName.isEmpty()) {
            throw new IllegalArgumentException("localName must not be empty");
        }
        this.value = ordinal();
        this.localName = localName;
        iri = IRI.create(PREFIX + localName);
        createDisplayName();
    }

    /**
     * Builds the display name (sequence of stars).
     */
    private void createDisplayName() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < value; i++) {
            sb.append(STAR_CHARACTER);
        }
        displayName = sb.toString();
    }

    /**
     * Gets the IRI for this StarRating.  The IRI is formed from the concatenation of the localName {@link #getLocalName()}
     * plus the prefix defined by the value of the {@link #PREFIX} field.
     * @return The IRI for this StarRating.
     */
    public IRI getIRI() {
        return iri;
    }

    /**
     * Gets the local name for this star rating.  The local name is used in combination with the prefix specified by
     * {@link #PREFIX} to construct an absolute IRI for this StarRating.
     * @return The local name.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Gets the value of this StarRating.  This is infact a "synonym method" for the {@link #ordinal()} method.
     * @return The value of this StarRating.  The value will &gt;=0 and &lt; {@link #values()} (i.e. less than the length
     * of the StarRating enum).
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the display value for this StarRating.  The display value is either an empty string (signifying no stars) or
     * a string consisting of star characters, defined by {@link #STAR_CHARACTER}, of length equal to {@link #getValue()}.
     * @return A display name for this StarRating.
     */
    public String getDisplayName() {
        return displayName;
    }


    /**
     * Determines if an IRI corresponds to a StarRating IRI.
     * @param iri The IRI to test.
     * @return <code>true</code> if the specified IRI is equal to the IRI of a StarRating, <code>false</code> if this
     * is not the case (or the specified IRI is <code>null</code>).
     */
    public static boolean isStarRatingIRI(IRI iri) {
        if(iri == null) {
            return false;
        }
        for(StarRating rating : values()) {
            if(rating.getIRI().equals(iri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the StarRating which has an IRI that is equal to the specified IRI.
     * @param iri The IRI to match against.
     * @return The StarRating which has an IRI equal to the specified IRI.
     * @throws RuntimeException if the specified IRI is not equal to the IRI of some StarRating.  Use 
     * {@link #isStarRatingIRI(org.semanticweb.owlapi.model.IRI)} to test to see whether or not this is the case before
     * calling this method.
     */
    public StarRating getStarRating(IRI iri) throws RuntimeException {
        for(StarRating starRating : values()) {
            if(starRating.getIRI().equals(iri)) {
                return starRating;
            }
        }
        throw new RuntimeException("The specified IRI (" + iri + ") is not equal to the IRI of any StarRating");
    }
    
    /**
     * Gets a StarRating given a value.
     * @param value The value of the StarRating.  The value must be &gt;= 0 and less than the length of the StarRating
     * enum.
     * @return The StarRating with the given value.
     * @throws IndexOutOfBoundsException if value &lt; 0, or value &gt;= the length of the StarRating enum.
     */
    public static StarRating getStarRating(int value) throws IndexOutOfBoundsException {
        for(StarRating starRating : values()) {
            if(starRating.value == value) {
                return starRating;
            }
        }
        throw new IndexOutOfBoundsException("value (" + value + ") does not match available StarRating values");
    }

    /**
     * Parses a string representation (the display name) of a StarRating.  The string representation must match the
     * regular expression specified by {@link #STAR_RATING_PATTERN}.
     * @param starRating The StarRating corresponding to the
     * @return The StarRating corresponding to the specified string.
     * @throws StarRatingFormatException if the specified starRating string does not match the regular expression specified by
     * {@link #STAR_RATING_PATTERN}.
     */
    public static StarRating parseStarRating(String starRating) throws StarRatingFormatException {
        Matcher matcher = STAR_RATING_PATTERN.matcher(starRating);
        if(!matcher.matches()) {
            throw new StarRatingFormatException(starRating);
        }
        String stars = matcher.group(STAR_RATING_PATTERN_STARS_GROUP);
        int numberOfStars = stars.length();
        return getStarRating(numberOfStars);
    }

    

}
