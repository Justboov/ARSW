/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.blacklistvalidator;

import edu.eci.arsw.spamkeywordsdatasource.HostBlacklistsDataSourceFacade;
import java.util.LinkedList;

/**
 *
 * @author jbojaca
 */
public class SearchThread extends Thread {
    
    private final HostBlacklistsDataSourceFacade skds=HostBlacklistsDataSourceFacade.getInstance();
    private LinkedList<Integer> blackListOcurrences=new LinkedList<>();
    private final String ipaddress;
    private final int numInicial;
    private final int numFinal;
    private int ocurrencesCount;
    private int checkedListsCount;
    
    public SearchThread (String ipaddress, int numInicial, int numFinal){
        this.ipaddress = ipaddress;
        this.numFinal = numFinal;
        this.numInicial = numInicial;
    }
    
    @Override
    public void run(){
        ocurrencesCount = 0;
        checkedListsCount = 0;
        for (int i=numInicial; i<numFinal;i++){
            checkedListsCount++;
            if (skds.isInBlackListServer(i, ipaddress)){
                blackListOcurrences.add(i);
                ocurrencesCount++;
            }
        }
        setBlackListOcurrences(blackListOcurrences);
        setCheckedListCount(checkedListsCount);
        setOcurrenceCount(ocurrencesCount);
    }
    
    public LinkedList<Integer> getBlackListOcurrences(){
        return blackListOcurrences;
    }
    
    public int getOcurrenceCount(){
        return ocurrencesCount;
    }
    
    public int getCheckedListCount() {
        return checkedListsCount;
    }
    
    private void setBlackListOcurrences(LinkedList<Integer> blackListOcurrences){
        this.blackListOcurrences = blackListOcurrences;
    }
    
    private void setOcurrenceCount(int ocurrenceCount){
        this.ocurrencesCount = ocurrenceCount;
    }
    
    private void setCheckedListCount(int checkedListCount){
        this.checkedListsCount = checkedListCount;
    }
}
