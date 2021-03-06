package censusanalyser.model;

import censusanalyser.CensusAdapter;

public class CensusDAO{
    public double area;
    public double density;
    public String state;
    public int population;
    public String stateId;

    public CensusDAO(IndiaCensusCSV census) {
        this.state  = census.state;
        this.population = census.population;
        this.area = census.areaInSqKm;
        this.density = census.densityPerSqKm;
    }

    public CensusDAO(USCensusCSV census) {
        this.state = census.state;
        this.stateId = census.stateId;
        this.area=census.totalArea;
        this.population=census.population;
        this.density=census.populationDensity;
    }

    public CensusDAO(CSVStates census) {
        this.state=census.state;
        this.stateId=census.stateCode;
    }

    public Object getCensusDTO(CensusAdapter.TYPE type) {
        if(type.equals(CensusAdapter.TYPE.INDIA)){
            return new IndiaCensusCSV(state,population,(int) density,(int) area);
        }else if(type.equals(CensusAdapter.TYPE.US)){
            return new USCensusCSV(state,stateId,population,(int) area,(int) density);
        }else{
            return new CSVStates(state,stateId);
        }
    }
}
