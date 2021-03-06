package censusanalyser.exception;

public class CensusAnalyserException extends RuntimeException {

    public ExceptionType type;

    public enum ExceptionType {
        CENSUS_FILE_PROBLEM,CENSUS_TYPE_PROBLEM, CENSUS_HEADER_PROBLEM, CENSUS_DELIMITER_PROBLEM,NO_CENSUS_DATA;
    }

    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }

}
