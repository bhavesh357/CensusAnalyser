package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder implements ICSVBuilder{
    @Override
    public Iterator getCSVFileIterator(Reader reader, Class csvStatesClass){
        try{
            return getCsvToBean(reader,csvStatesClass).iterator();
        }catch (Exception e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }

    @Override
    public List getCSVFileList(Reader reader, Class className) {
        try{
            return getCsvToBean(reader,className).parse();
        }catch (RuntimeException e){
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
