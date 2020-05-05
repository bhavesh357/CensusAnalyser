package censusanalyser.CSVClasses;

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
}
