package censusanalyser;

import censusanalyser.model.*;
import censusanalyser.exception.CensusAnalyserException;
import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser<E>{

    private CensusAdapter.TYPE type;
    Map<String, CensusDAO> censusCSVMap=null;
    List<CensusDAO> censusCSVList=null;

    public CensusAnalyser() {
        censusCSVMap=new HashMap<String, CensusDAO>();
        censusCSVList=new ArrayList<CensusDAO>();
    }

    public int loadIndiaCensusData(Class indiaCensusCSVClass, char c, String... csvFilePath) throws CensusAnalyserException {
        type= CensusAdapter.TYPE.INDIA;
        censusCSVMap=CensusAdapterFactory.getCensusData(type,indiaCensusCSVClass,c,csvFilePath);
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
        type= CensusAdapter.TYPE.US;
        censusCSVMap= CensusAdapterFactory.getCensusData(type,USCensusCSV.class,',',csvFilePath);
        censusCSVList.addAll(censusCSVMap.values());
        return censusCSVMap.size();
    }

    public int loadIndiaStateCodeData(Class<E> indiaCensusCSVClass,char c,String csvFilePath) throws CensusAnalyserException {
        type= CensusAdapter.TYPE.INDIA_STATE;
        censusCSVMap= CensusAdapterFactory.getCensusData(type,CSVStates.class,c,csvFilePath);
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
        checkIfNull(censusCSVMap);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.state);
        ArrayList censusList= getSortedArray(comparing);
        return new Gson().toJson(censusList);
    }

    public String getPopulationWiseSortedCensusData() {
        checkIfNull(censusCSVMap);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.population);
        ArrayList censusList= getSortedArray(comparing);
        return new Gson().toJson(censusList);
    }

    public String getPopulationDensityWiseSortedCensusData() {
        checkIfNull(censusCSVMap);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.density);
        ArrayList censusList= getSortedArray(comparing);
        return new Gson().toJson(censusList);
    }

    public String getAreaWiseSortedCensusData() {
        checkIfNull(censusCSVMap);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.area);
        ArrayList censusList= getSortedArray(comparing.reversed());
        return new Gson().toJson(censusList);
    }

    public String getStateCodeWiseSortedCensusData() {
        checkIfNull(censusCSVMap);
        Comparator<CensusDAO> comparing = Comparator.comparing(census -> census.stateId);
        ArrayList censusList= getSortedArray(comparing);
        return new Gson().toJson(censusList);
    }

    private void checkIfNull(Map<String, CensusDAO> list){
        if(list == null || list.size()==0){
            throw new CensusAnalyserException("No Census Data",CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
    }

    private String getJson(List list){
        String json = new Gson().toJson(list);
        return json;
    }

    private ArrayList getSortedArray(Comparator<CensusDAO> comparing) {
        return censusCSVMap.values().stream().sorted(comparing).map(censusDAO -> censusDAO.getCensusDTO(type)).collect(Collectors.toCollection(ArrayList::new));
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
