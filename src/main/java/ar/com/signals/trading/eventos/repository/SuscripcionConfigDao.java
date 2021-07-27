package ar.com.signals.trading.eventos.repository;

import java.util.List;

import ar.com.signals.trading.eventos.domain.SuscripcionConfig;
import ar.com.signals.trading.util.repository.GenericDao;

public interface SuscripcionConfigDao extends GenericDao<SuscripcionConfig> {

	List<SuscripcionConfig> getTradingAlertSuscript(long minutos);

}