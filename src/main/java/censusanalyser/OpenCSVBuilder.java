package censusanalyser;
import censusanalyser.CSVClasses.CSVStates;
import censusanalyser.CSVClasses.IndiaCensusCSV;
import censusanalyser.exception.CSVBuilderException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OpenCSVBuilder <E> implements ICSVBuilder{
    @Override
    public Iterator getCSVFileIterator(Reader reader, Class csvStatesClass){
        try{
            return getCsvToBean(reader,csvStatesClass).iterator();
        }catch (Exception e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }

    /*
    @Override
    public List getCSVFileList(Reader reader, Class className) {
        try{
            return getCsvToBean(reader,className).parse();
        }catch (RuntimeException e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }


    @Override
    public Map getCSVFileMapCensus(Reader reader, Class className) {
        try{
            List<IndiaCensusCSV> list=getCsvToBean(reader, className).parse();
            Map map = new HashMap<String,IndiaCensusCSV>();
            for(IndiaCensusCSV e:list){
                map.put(e.state, e);
            }
            return map;
        }catch (RuntimeException e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }

    @Override
    public Map getCSVFileMapState(Reader reader, Class className) {
        try{
            List<CSVStates> list=getCsvToBean(reader, className).parse();
            Map map = new HashMap<String,CSVStates>();
            for(CSVStates e:list){
                map.put(e.state, e);
            }
            return map;
        }catch (RuntimeException e){
            throw new CSVBuilderException(e.getMessage(),CSVBuilderException.ExceptionType.CENSUS_HEADER_PROBLEM);
        }
    }
    */

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
