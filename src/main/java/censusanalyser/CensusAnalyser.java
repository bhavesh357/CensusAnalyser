package censusanalyser;

import censusanalyser.model.*;
import censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.util.*;

public class CensusAnalyser<E>{

    Map<String, CensusDAO> censusCSVMap=null;
    List<CensusDAO> censusCSVList=null;
    Map<String, CensusDAO> censusStateMap=null;
    List<CensusDAO> censusList=null;

    public CensusAnalyser() {
        censusList=new ArrayList<CensusDAO>();
        censusStateMap = new HashMap<String, CensusDAO>();
        censusCSVMap=new HashMap<String, CensusDAO>();
        censusCSVList=new ArrayList<CensusDAO>();
    }

    public int loadIndiaCensusData(Class indiaCensusCSVClass, char c, String... csvFilePath) throws CensusAnalyserException {
        censusCSVMap=new CensusLoader().loadCensusData(indiaCensusCSVClass,c,csvFilePath);
        censusCSVList.addAll(censusCSVMap.values());
        return censusCSVMap.size();
    }

    public int loadIndiaCensusData(Class indiaCensusCSVClass,String... csvFilePath){
        return loadIndiaCensusData(indiaCensusCSVClass,',',csvFilePath);
    }

    public int loadIndiaCensusData(String... csvFilePath){
        return loadIndiaCensusData(IndiaCensusCSV.class,',',csvFilePath);
    }

    public int loadUSCensusData(String csvFilePath) {
        censusCSVMap= new CensusLoader().loadCensusData(USCensusCSV.class,',',csvFilePath);
        censusCSVList.addAll(censusCSVMap.values());
        return censusCSVMap.size();
    }

    public int loadIndiaStateCodeData(Class<E> indiaCensusCSVClass,char c,String csvFilePath) throws CensusAnalyserException {
        censusCSVMap= new CensusLoader().loadCensusData(CSVStates.class,',',csvFilePath);
        censusCSVList.addAll(censusCSVMap.values());
        return censusCSVMap.size();
    }

    public int loadIndiaStateCodeData(Class indiaCensusCSVClass,String... csvFilePath){
        return loadIndiaCensusData(indiaCensusCSVClass,',',csvFilePath);
    }

    public int loadIndiaStateCodeData(String... csvFilePath){
        return loadIndiaCensusData(IndiaCensusCSV.class,',',csvFilePath);
    }

    public String getStateWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.state);
        this.sort(comparing,censusCSVList,true);
        return getJson(censusCSVList);
    }
    public String getPopulationWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.population);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getPopulationDensityWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.density);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getAreaWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.area);
        this.sort(comparing,censusCSVList,false);
        return getJson(censusCSVList);
    }

    public String getStateCodeWiseSortedCensusData() {
        checkIfNull(censusCSVList);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.stateId);
        this.sort(comparing,censusCSVList,true);
        return getJson(censusCSVList);
    }

    private void checkIfNull(List list){
        if(list == null || list.size()==0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }

    private String getJson(List list){
        String json = new Gson().toJson(list);
        return json;
    }

    private void sort(Comparator comparing,List list,boolean isAscending) {
        for(int i=0;i< list.size()-1;i++){
            for(int j=0; j < list.size()-i-1 ;j++){
                Object census1= list.get(j);
                Object  census2 = list.get(j+1);
                if(isAscending==true){
                    if(comparing.compare(census1,census2)>0){
                        list.set(j,census2);
                        list.set(j+1,census1);
                    }
                }else {
                    if (comparing.compare(census1, census2) < 0) {
                        list.set(j, census2);
                        list.set(j + 1, census1);
                    }
                }
            }
        }
    }
}
