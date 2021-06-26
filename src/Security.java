public class Security {

    private String ISIN;
    private String FX;
    private String name_of_security;
    String url = URLGetter.getURLContents("https://markets.ft.com/data/funds/tearsheet/summary?s="+this.getISIN()+":"+this.getFX());

    public void setName(String newName){

        this.name_of_security = newName;
    }

    public void setFX(String newFx){
        this.FX = newFx;
    }

    public void setISIN(String newISIN){
        this.ISIN = newISIN;
    }

    public String getName(){
        return this.name_of_security;
    }

    public String getFX(){
        return this.FX;
    }

    public String getISIN(){
        return this.ISIN;
    }



}
