/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smchart.indicators;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.data.xy.OHLCDataItem;
import org.smchart.gui.IndicatorOptions;
import org.jfree.chart.plot.XYPlot;
import org.smchart.indicators.IndicatorFactory.IndicatorType;
import org.smchart.xml.ChartIndicator;


/**
 *
 * @author siva
 */
public class IndicatorFactory {

    public enum IndicatorType {

        STOCHASTIC, BOLLINGER, RSI, SMA, EMA, MACD, VOLUME, PARABOLICSAR
    }
    private String m_ticker;
    private OHLCDataItem[] m_ohlcDataArr;
    private CombinedDomainXYPlot m_combinedDomainXYPlot;
    private Hashtable m_inputHash;

    public IndicatorFactory(CombinedDomainXYPlot comDomainPlot, OHLCDataItem[] ohlcDataArr, String ticker) {
        m_ohlcDataArr = ohlcDataArr;
        m_ticker = ticker;
        m_combinedDomainXYPlot = comDomainPlot;
        m_inputHash = new Hashtable();
    }
    
    public void removeUpperInidicator(IndicatorType type) throws Exception {
        IndicatorOptions op = new IndicatorOptions();
        op.setFAvg("0");
        op.setSAvg("0");
        op.setTAvg("0");
        switch (type) {
            case SMA:
                addUpperIndicator(type, op);
                break;
            case EMA:
                addUpperIndicator(type, op);
            case BOLLINGER:
                addUpperIndicator(type, op);
                break;
            case PARABOLICSAR:
                addUpperIndicator(type, op);
                break;
        }
    }

  public static void updateIndicator(XYPlot indPlot)
  {
    IndicatorOptions op = new IndicatorOptions();

    if ((indPlot instanceof Stochastic)) {
      AbstractIndicatorInput input = ((Stochastic)indPlot).getChartInput();
      op.setFAvg("" + ((StochasticInput)input).getTimePeriod());
      op.setSAvg("" + ((StochasticInput)input).getMovingAvg());
      op.displayOptions(IndicatorType.STOCHASTIC);
      if (op.isOkToAddIndicator()) {
        ((Stochastic)indPlot).updateChartPlot(new StochasticInput(Integer.parseInt(op.getSAvg()), Integer.parseInt(op.getFAvg())));
      }

    }
    else if ((indPlot instanceof RSI)) {
      AbstractIndicatorInput input = ((RSI)indPlot).getChartInput();
      op.setFAvg("" + ((RSIInput)input).getTimePeriod());
      op.displayOptions(IndicatorType.RSI);
      if (op.isOkToAddIndicator()) {
        ((RSI)indPlot).updateChartPlot(new RSIInput(Integer.parseInt(op.getFAvg())));
      }
    }
    else if ((indPlot instanceof MACD)) {
      AbstractIndicatorInput input = ((MACD)indPlot).getChartInput();
      op.setFAvg("" + ((MACDInput)input).getSlowMA());
      op.setSAvg("" + ((MACDInput)input).getFastMA());
      op.setTAvg("" + ((MACDInput)input).getEMATimePeriod());
      op.displayOptions(IndicatorType.MACD);
      if (op.isOkToAddIndicator())
        ((MACD)indPlot).updateChartPlot(new MACDInput(Integer.parseInt(op.getFAvg()), Integer.parseInt(op.getSAvg()), Integer.parseInt(op.getTAvg())));
    }
  }
    public boolean addIndicator(IndicatorType type) throws Exception {
        boolean result = false;
        IndicatorOptions op = new IndicatorOptions();
        switch (type) {
            case STOCHASTIC:
                op.setFAvg("14");
                op.setSAvg("3");
                op.displayOptions(IndicatorType.STOCHASTIC);
                if (op.isOkToAddIndicator()) {
                    addLowerIndicator(type, op);
                }
                break;
            case VOLUME:
                addLowerIndicator(type, null);
                break;
            case RSI:
                op.setFAvg("14");
                op.displayOptions(IndicatorType.RSI);
                if (op.isOkToAddIndicator()) {
                    addLowerIndicator(type, op);
                }
                break;
            case MACD:
                op.setFAvg("26");
                op.setSAvg("12");
                op.setTAvg("9");
                op.displayOptions(IndicatorType.MACD);
                if (op.isOkToAddIndicator()) {
                    addLowerIndicator(type, op);
                }
                break;
            case SMA:
                op.setFAvg("50");
                op.setSAvg("100");
                op.setTAvg("200");
                op.displayOptions(IndicatorType.SMA);
                if (op.isOkToAddIndicator()) {
                    addUpperIndicator(type, op);
                    result = true;
                }
                break;
            case EMA:
                op.setFAvg("50");
                op.setSAvg("100");
                op.setTAvg("200");
                op.displayOptions(IndicatorType.EMA);
                if (op.isOkToAddIndicator()) {
                    addUpperIndicator(type, op);
                    result = true;
                }
                break;
            case BOLLINGER:
                op.setFAvg("20");
                op.setSAvg("2");
                op.displayOptions(IndicatorType.BOLLINGER);
                if (op.isOkToAddIndicator()) {
                    addUpperIndicator(type, op);
                    result = true;
                }
                break;
            case PARABOLICSAR:
                op.setFAvg("0.02");
                op.setSAvg("0.02");
                op.setTAvg("0.2");
                op.displayOptions(IndicatorType.PARABOLICSAR);
                if (op.isOkToAddIndicator()) {
                    addUpperIndicator(type, op);
                    result = true;
                }
                break;
        }
        return result;
    }

    private void addLowerIndicator(IndicatorType type, IndicatorOptions op) throws Exception {
        AbstractIndicator indObj = null;
        switch (type) {
            case STOCHASTIC:
                indObj = new Stochastic(m_ohlcDataArr, m_ticker, Stochastic.StochasticType.Fast);
//                        StochasticInput inpt = (StochasticInput) stoc.getChartInput();
//                        op.setFAvg("" + inpt.getTimePeriod());
//                        op.setSAvg("" + inpt.getMovingAvg());                    

                if (m_combinedDomainXYPlot.getSubplots().contains(indObj.getSubPlot())) {
                    m_combinedDomainXYPlot.remove(indObj.getSubPlot());
                }
                indObj.addChartPlot(m_combinedDomainXYPlot, new StochasticInput(Integer.parseInt(op.getSAvg()),
                        Integer.parseInt(op.getFAvg())));

                break;
            case VOLUME:
                indObj = new Volume(m_ohlcDataArr, m_ticker);
                if (m_combinedDomainXYPlot.getSubplots().contains(indObj.getSubPlot())) {
                    m_combinedDomainXYPlot.remove(indObj.getSubPlot());
                }
                indObj.addChartPlot(m_combinedDomainXYPlot, null);
                break;
            case MACD:
                indObj = new MACD(m_ohlcDataArr, m_ticker);
//                        MACDInput inpt = (MACDInput) macd.getChartInput();
//                        op.setFAvg("" + inpt.getSlowMA());
//                        op.setSAvg("" + inpt.getFastMA());
//                        op.setTAvg("" + inpt.getEMATimePeriod());                
                    if (m_combinedDomainXYPlot.getSubplots().contains(indObj.getSubPlot())) {
                        m_combinedDomainXYPlot.remove(indObj.getSubPlot());
                    }
                    indObj.addChartPlot(m_combinedDomainXYPlot,
                            new MACDInput(Integer.parseInt(op.getFAvg()), Integer.parseInt(op.getSAvg()),
                            Integer.parseInt(op.getTAvg())));//slow,fast,ema
                
                break;
            case RSI:

                indObj = new RSI(m_ohlcDataArr, m_ticker);
//                        RSIInput inpt = (RSIInput) rs.getChartInput();
//                        op.setFAvg("" + inpt.getTimePeriod());                
                    if (m_combinedDomainXYPlot.getSubplots().contains(indObj.getSubPlot())) {
                        m_combinedDomainXYPlot.remove(indObj.getSubPlot());
                    }
                    indObj.addChartPlot(m_combinedDomainXYPlot, new RSIInput(Integer.parseInt(op.getFAvg())));
                
                break;
        }
        
    }
  public void ClearIndicatorHash() {
    if (this.m_inputHash != null) {
      this.m_inputHash.clear();
      this.m_inputHash = null;
    }
  }
  public void restoreIndicators(ArrayList<ChartIndicator> indList) {
    if (indList != null) {
    Iterator iter = indList.iterator();
    AbstractIndicator indObj = null;
    while (iter.hasNext()) {
      ChartIndicator ind = (ChartIndicator)iter.next();
      switch (IndicatorType.valueOf(ind.getM_indName()).ordinal()) {
      case 3:
        indObj = new BollingerBands(this.m_ohlcDataArr, this.m_ticker);
        indObj.addChartPlot(this.m_combinedDomainXYPlot, new BollingerInput((int)ind.getM_var2(), (int)ind.getM_var1()));

        break;
      case 1:
        indObj = new SMA(this.m_ohlcDataArr, this.m_ticker);
        int[] maArr = { (int)ind.getM_var1(), (int)ind.getM_var2(), (int)ind.getM_var3() };

        indObj.addChartPlot(this.m_combinedDomainXYPlot, new SMAInput(maArr));

        break;
      case 2:
        indObj = new EMA(this.m_ohlcDataArr, this.m_ticker);

        int[] emaArr = { (int)ind.getM_var1(), (int)ind.getM_var2(), (int)ind.getM_var3() };

        indObj.addChartPlot(this.m_combinedDomainXYPlot, new EMAInput(emaArr));

        break;
      case 4:
        indObj = new ParabolicSAR(this.m_ohlcDataArr, this.m_ticker);

        indObj.addChartPlot(this.m_combinedDomainXYPlot, new ParabolicSARInput(ind.getM_var1(), ind.getM_var2(), ind.getM_var3()));
      }

      if (!this.m_inputHash.containsKey(ind.getM_indName())) {
        this.m_inputHash.put(ind.getM_indName(), indObj.getChartInput());
      } else {
        this.m_inputHash.remove(ind.getM_indName());
        this.m_inputHash.put(ind.getM_indName(), indObj.getChartInput());
      }
    }
  }
  }
  public ArrayList<ChartIndicator> getChartIndicatorList() {
    ArrayList indList = null;
    try {
      if ((this.m_inputHash != null) && (this.m_inputHash.size() > 0)) {
        indList = new ArrayList();
        Enumeration enumUpperInd = this.m_inputHash.keys();
        while (enumUpperInd.hasMoreElements()) {
          ChartIndicator ci = new ChartIndicator();
          ci.setM_ticker(this.m_ticker);
          String type = enumUpperInd.nextElement().toString();
          ci.setM_indName(type);
          switch (IndicatorType.valueOf(type).ordinal()) {
          case 1:
            ci.setM_var1(((SMAInput)this.m_inputHash.get(type)).getMovingAvg1());
            ci.setM_var2(((SMAInput)this.m_inputHash.get(type)).getMovingAvg2());
            ci.setM_var3(((SMAInput)this.m_inputHash.get(type)).getMovingAvg3());
            break;
          case 2:
            ci.setM_var1(((EMAInput)this.m_inputHash.get(type)).getMovingAvgArr()[0]);
            ci.setM_var2(((EMAInput)this.m_inputHash.get(type)).getMovingAvgArr()[1]);
            ci.setM_var3(((EMAInput)this.m_inputHash.get(type)).getMovingAvgArr()[2]);
            break;
          case 3:
            ci.setM_var1(((BollingerInput)this.m_inputHash.get(type)).getMovingAverage());
            ci.setM_var2(((BollingerInput)this.m_inputHash.get(type)).getStdDev());
            break;
          case 4:
            ci.setM_var1(((ParabolicSARInput)this.m_inputHash.get(type)).getInitialValue());
            ci.setM_var2(((ParabolicSARInput)this.m_inputHash.get(type)).getStepValue());
            ci.setM_var3(((ParabolicSARInput)this.m_inputHash.get(type)).getMaximumValue());
          }

          indList.add(ci);
        }
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return indList;
  }
    private void addUpperIndicator(IndicatorType type, IndicatorOptions op) throws Exception {
        AbstractIndicator indObj = null;
        switch (type) {
            case BOLLINGER:
                indObj = new BollingerBands(m_ohlcDataArr, m_ticker);

//                        BollingerInput inpt = (BollingerInput) bbChart.getChartInput();
//                        op.setFAvg("" + inpt.getMovingAverage());
//                        op.setSAvg("" + inpt.getStdDev());
                    indObj.addChartPlot(m_combinedDomainXYPlot,
                            new BollingerInput(Integer.parseInt(op.getSAvg()), Integer.parseInt(op.getFAvg())));                
                break;
            case SMA:
                indObj = new SMA(m_ohlcDataArr, m_ticker);

//                        SMAInput inpt = (SMAInput) smChr.getChartInput();
//                        op.setFAvg("" + inpt.getMovingAvgArray()[0]);
//                        op.setSAvg("" + inpt.getMovingAvgArray()[1]);
//                        op.setTAvg("" + inpt.getMovingAvgArray()[2]);                
                    int[] maArr = {Integer.parseInt(op.getFAvg()), Integer.parseInt(op.getSAvg()),
                        Integer.parseInt(op.getTAvg())
                    };
                    indObj.addChartPlot(m_combinedDomainXYPlot,
                            new SMAInput(maArr));                
                break;
            case EMA:

                indObj = new EMA(m_ohlcDataArr, m_ticker);

//                        EMAInput inpt = (EMAInput) emChr.getChartInput();
//                        op.setFAvg("" + inpt.getMovingAvgArr()[0]);
//                        op.setSAvg("" + inpt.getMovingAvgArr()[1]);
//                        op.setTAvg("" + inpt.getMovingAvgArr()[2]);
                    int[] emaArr = {Integer.parseInt(op.getFAvg()), Integer.parseInt(op.getSAvg()),
                        Integer.parseInt(op.getTAvg())
                    };
                    indObj.addChartPlot(m_combinedDomainXYPlot,
                            new EMAInput(emaArr));
                
                break;
            case PARABOLICSAR:
                indObj = new ParabolicSAR(m_ohlcDataArr, m_ticker);
//                        ParabolicSARInput inpt = (ParabolicSARInput) sarChr.getChartInput();
//                        op.setFAvg("" + inpt.getInitialValue());
//                        op.setSAvg("" + inpt.getStepValue());
//                        op.setTAvg("" + inpt.getMaximumValue());
                    indObj.addChartPlot(m_combinedDomainXYPlot, new ParabolicSARInput(0.0, 0.0, 0.0));
                    indObj.addChartPlot(m_combinedDomainXYPlot,
                            new ParabolicSARInput(Double.parseDouble(op.getFAvg()), Double.parseDouble(op.getSAvg()),
                            Double.parseDouble(op.getTAvg())));                
                break;
        }
    }
}
