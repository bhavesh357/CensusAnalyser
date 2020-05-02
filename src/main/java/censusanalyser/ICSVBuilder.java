package censusanalyser;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICSVBuilder <E>{
    Iterator<E> getCSVFileIterator(Reader reader, Class<E> csvStatesClass);

    List<E> getCSVFileList(Reader reader, Class<E> indiaCensusCSVClass);
}