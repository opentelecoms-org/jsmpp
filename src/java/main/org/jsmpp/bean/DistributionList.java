package org.jsmpp.bean;


/**
 * @author uudashr
 *
 */
public class DistributionList implements DestinationAddress {
    private final String name;

    public DistributionList(String name) {
        this.name = name;
    }
    
    public Flag getFlag() {
        return Flag.DISTRIBUTION_LIST;
    }
    
    public String getName() {
        return name;
    }
}
