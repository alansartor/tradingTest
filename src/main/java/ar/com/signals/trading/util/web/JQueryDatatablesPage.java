package ar.com.signals.trading.util.web;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class JQueryDatatablesPage <T> implements java.io.Serializable {

    private final int iTotalRecords;
    private final int iTotalDisplayRecords;
    private final String sEcho;
    private final Collection<T> aaData;
    
    private final Object extra;//datos extra, lo voy a usr en algunas paginas para mostrar datos en el footer

    public JQueryDatatablesPage(final Collection<T> pageContent,
            final int iTotalRecords,
            final int iTotalDisplayRecords,
            final String sEcho){

        this.aaData = pageContent;
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.sEcho = sEcho;
        this.extra = null;
    }
    
    public JQueryDatatablesPage(final Collection<T> pageContent,
            final int iTotalRecords,
            final int iTotalDisplayRecords,
            final String sEcho, final Object extra){

        this.aaData = pageContent;
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.sEcho = sEcho;
        this.extra = extra;
    }

    public int getiTotalRecords(){
        return this.iTotalRecords;
    }

    public int getiTotalDisplayRecords(){
        return this.iTotalDisplayRecords;
    }

    public String getsEcho(){
        return this.sEcho;
    }

    public Collection<T> getaaData(){
        return this.aaData;
    }

	public Object getExtra() {
		return extra;
	}
}
