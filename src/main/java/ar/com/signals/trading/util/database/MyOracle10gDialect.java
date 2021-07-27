package ar.com.signals.trading.util.database;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class MyOracle10gDialect extends Oracle10gDialect{
   public MyOracle10gDialect() {
        super();
      //Para poder usar esta funcion nativa con hql
        registerFunction(
                "regexp_like", new SQLFunctionTemplate(StandardBasicTypes.BOOLEAN,
                "(case when (regexp_like(?1, ?2, ?3)) then 1 else 0 end)")
              );
        
	    //registerFunction("regexp_like", new VarArgsSQLFunction(new BooleanType(), "REGEXP_LIKE", ",", ")"));
	    //registerFunction("regexp_like", new StandardSQLFunction("REGEXP_LIKE", new BooleanType()));
	    //registerFunction("regexp_like", new SQLFunctionTemplate(new BooleanType(),"REGEXP_LIKE(?1, ?2, ?3)"));
    }
}
