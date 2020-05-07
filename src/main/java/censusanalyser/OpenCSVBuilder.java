package censusanalyser;
import censusanalyser.csvBuilder.ICSVBuilder;
import censusanalyser.exception.CSVBuilderException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class OpenCSVBuilder <E> implements ICSVBuilder {
    @Override
    public Iterator getCSVFileIterator(Reader reader, Class csvStatesClass){
        try{
            return getCsvToBean(reader,csvStatesClass).iterator();
        }catch (Exception e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }

    private CsvToBean getCsvToBean(Reader reader, Class className) {
        try{
            return (CsvToBean) new CsvToBeanBuilder<>(reader)
                    .withType(className)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build();
        }catch (RuntimeException e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }
}
