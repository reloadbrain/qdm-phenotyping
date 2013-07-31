package edu.mayo.qdm.demographics;

import edu.mayo.qdm.demographics.model.DemographicCategory;
import edu.mayo.qdm.demographics.model.DemographicStat;
import edu.mayo.qdm.demographics.model.DemographicType;
import edu.mayo.qdm.demographics.model.Demographics;
import edu.mayo.qdm.patient.Patient;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 */
public class DemographicsProcessor {

    public Demographics getDemographics(Map<String,Iterable<Patient>> results){
        Map<AbstractDemographicsItem,Integer> counter = new HashMap<AbstractDemographicsItem,Integer>();

        for(Map.Entry<String, Iterable<Patient>> entrySet : results.entrySet()){
            for(Patient patient : entrySet.getValue()){
                Set<AbstractDemographicsItem> items =
                    this.getDemographicItems(entrySet.getKey(), patient);

                for(AbstractDemographicsItem item : items){
                    if(! counter.containsKey(item)){
                        counter.put(item, 0);
                    }

                    int count = counter.get(item);
                    counter.put(item, count + 1);
                }
            }
        }

        Demographics demographics = new Demographics();

        for(Map.Entry<AbstractDemographicsItem, Integer> entrySet : counter.entrySet()){
            AbstractDemographicsItem item = entrySet.getKey();

            DemographicStat stat = new DemographicStat();
            stat.setLabel(item.getLabel());
            stat.setValue(BigInteger.valueOf(entrySet.getValue()));

            DemographicCategory category = new DemographicCategory();
            category.getDemographicStat().add(stat);

            DemographicType type = new DemographicType();
            type.getDemographicCategory().add(category);

            demographics.getDemographicType().add(type);
        }

        return demographics;
    }

    protected Set<AbstractDemographicsItem> getDemographicItems(String population, Patient patient){
        Set<AbstractDemographicsItem> itemSet = new HashSet<AbstractDemographicsItem>();

        itemSet.add(new AgeDemographicsItem(population, patient));
        itemSet.add(new GenderDemographicsItem(population, patient));
        itemSet.add(new RaceDemographicsItem(population, patient));
        itemSet.add(new EthnicityDemographicsItem(population, patient));

        return itemSet;
    }

}