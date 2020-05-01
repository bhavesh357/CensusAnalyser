package censusanalyser;

public class CSVBuilderException extends RuntimeException {
    enum ExceptionType {
        CENSUS_FILE_PROBLEM,CENSUS_TYPE_PROBLEM, CENSUS_HEADER_PROBLEM, CENSUS_DELIMITER_PROBLEM
    }

    CSVBuilderException.ExceptionType type;

    public CSVBuilderException(String message, CSVBuilderException.ExceptionType type) {
        super(message);
        this.type = type;
    }
}
