/*
 * ADagentView.java
 */

package adagent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.Task;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.RollbackException;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.beansbinding.AbstractBindingListener;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.PropertyStateEvent;

/**
 * The application's main frame.
 */
public class ADagentView extends FrameView {
    
    public ADagentView(SingleFrameApplication app) {
        super(app);

        initComponents();
        initComponents2();
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
	messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
	messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        }); 
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        // tracking table selection
        masterTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    firePropertyChange("recordSelected", !isRecordSelected(), isRecordSelected());
                }
            });
        detailTable.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    firePropertyChange("detailRecordSelected", !isDetailRecordSelected(), isDetailRecordSelected());
                }
            });

        // tracking changes to save
        bindingGroup.addBindingListener(new AbstractBindingListener() {
            @Override
            public void targetChanged(Binding binding, PropertyStateEvent event) {
                // save action observes saveNeeded property
                setSaveNeeded(true);
            }
        });

        // have a transaction started
        entityManager.getTransaction().begin();
    }


    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    private void setSaveNeeded(boolean saveNeeded) {
        if (saveNeeded != this.saveNeeded) {
            this.saveNeeded = saveNeeded;
            firePropertyChange("saveNeeded", !saveNeeded, saveNeeded);
        }
    }

    public boolean isRecordSelected() {
        return masterTable.getSelectedRow() != -1;
    }
    
    public boolean isDetailRecordSelected() {
        return detailTable.getSelectedRow() != -1;
    }

    public boolean isOrderRecordSelected() {
        return orderTable.getSelectedRow() != -1;
    }

    @Action
    public void newRecord() {
        adagent.TEmployee T = new adagent.TEmployee();
        entityManager.persist(T);
        list.add(T);
        int row = list.size()-1;
        masterTable.setRowSelectionInterval(row, row);
        masterTable.scrollRectToVisible(masterTable.getCellRect(row, 0, true));
        setSaveNeeded(true);
    }

    @Action(enabledProperty = "recordSelected")
    public void deleteRecord() {
        int[] selected = masterTable.getSelectedRows();
        List<adagent.TEmployee> toRemove = new ArrayList<adagent.TEmployee>(selected.length);
        for (int idx=0; idx<selected.length; idx++) {
            adagent.TEmployee T = list.get(masterTable.convertRowIndexToModel(selected[idx]));
            removeAllOrder(T);
            toRemove.add(T);
            entityManager.remove(T);
        }
        list.removeAll(toRemove);
        setSaveNeeded(true);
    }

    private void removeAllOrder(TEmployee T) {
        Collection<adagent.TOrder> ts = T.getTOrderList();
        int[] selected = new int[detailTable.getRowCount()];
        for( int idx = 0 ; idx<selected.length ; idx++) selected[idx] = idx;
        List<adagent.TOrder> toRemove = new ArrayList<adagent.TOrder>(selected.length);
        for (int idx=0; idx<selected.length; idx++) {
            selected[idx] = detailTable.convertRowIndexToModel(selected[idx]);
            int count = 0;
            Iterator<adagent.TOrder> iter = ts.iterator();
            while (count++ < selected[idx]) iter.next();
            adagent.TOrder t = iter.next();
            toRemove.add(t);
            entityManager.remove(t);
        }
        ts.removeAll(toRemove);
    }
    
    @Action(enabledProperty = "detailRecordSelected")
    public void editDetailRecord() {
        int index = masterTable.getSelectedRow();
        adagent.TEmployee T = list.get(masterTable.convertRowIndexToModel(index));
        int selected = detailTable.getSelectedRow();
        TOrder editOrder = T.getTOrderList().get(detailTable.convertRowIndexToModel(selected));

        if(doEditDialog(editOrder) == 0)
            setSaveNeeded(true);
    }

    @Action(enabledProperty = "detailRecordSelected")
    public void deleteDetailRecord() {
        int index = masterTable.getSelectedRow();
        adagent.TEmployee T = list.get(masterTable.convertRowIndexToModel(index));
        Collection<adagent.TOrder> ts = T.getTOrderList();
        int[] selected = detailTable.getSelectedRows();
        List<adagent.TOrder> toRemove = new ArrayList<adagent.TOrder>(selected.length);
        for (int idx=0; idx<selected.length; idx++) {
            selected[idx] = detailTable.convertRowIndexToModel(selected[idx]);
            int count = 0;
            Iterator<adagent.TOrder> iter = ts.iterator();
            while (count++ < selected[idx]) iter.next();
            adagent.TOrder t = iter.next();
            toRemove.add(t);
            entityManager.remove(t);
        }
        ts.removeAll(toRemove);
        masterTable.clearSelection();
        masterTable.setRowSelectionInterval(index, index);
        setSaveNeeded(true);
    }

    @Action(enabledProperty = "saveNeeded")
    public Task save() {
        return new SaveTask(getApplication());
    }

    private class SaveTask extends Task {
        SaveTask(org.jdesktop.application.Application app) {
            super(app);
        }
        @Override protected Void doInBackground() {
            try {
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            } catch (RollbackException rex) {
                rex.printStackTrace();
                entityManager.getTransaction().begin();
                List<adagent.TEmployee> merged = new ArrayList<adagent.TEmployee>(list.size());
                for (adagent.TEmployee T : list) {
                    merged.add(entityManager.merge(T));
                }
                list.clear();
                list.addAll(merged);
            }
            return null;
        }
        @Override protected void finished() {
            setSaveNeeded(false);
        }
    }

    /**
     * An example action method showing how to create asynchronous tasks
     * (running on background) and how to show their progress. Note the
     * artificial 'Thread.sleep' calls making the task long enough to see the
     * progress visualization - remove the sleeps for real application.
     */
    @Action
    public Task refresh() {
       return new RefreshTask(getApplication());
    }

    private class RefreshTask extends Task {
        RefreshTask(org.jdesktop.application.Application app) {
            super(app);
        }
        @SuppressWarnings("unchecked")
        @Override protected Void doInBackground() {
            try {
                setProgress(0, 0, 4);
                setMessage("Rolling back the current changes...");
                setProgress(1, 0, 4);
                entityManager.getTransaction().rollback();
                Thread.sleep(1000L); // remove for real app
                setProgress(2, 0, 4);

                setMessage("Starting a new transaction...");
                entityManager.getTransaction().begin();
                Thread.sleep(500L); // remove for real app
                setProgress(3, 0, 4);

                setMessage("Fetching new data...");
                java.util.Collection data, cdata, pdata, sdata;
                if(isAdminMode) {
                    data = query.getResultList();
                    for (Object entity : data) {
                        entityManager.refresh(entity);
                    }
                    cdata = tContTypeQuery.getResultList();
                    for (Object entity : cdata) {
                        entityManager.refresh(entity);
                    }
                    pdata = tPageTypeQuery.getResultList();
                    for (Object entity : pdata) {
                        entityManager.refresh(entity);
                    }
                    sdata = tSizeTypeQuery.getResultList();
                    for (Object entity : sdata) {
                        entityManager.refresh(entity);
                    }
                }
                else{
                    tOrderQuery.setParameter(1, currentUser.getEID());
                    data = tOrderQuery.getResultList();
                    for (Object entity : data) {
                        System.out.println(entity.toString());
                        entityManager.refresh(entity);
                    }
                    cdata = pdata = sdata = null;
                }
                Thread.sleep(1300L); // remove for real app
                setProgress(4, 0, 4);

                Thread.sleep(150L); // remove for real app
                if(isAdminMode){
                    list.clear();
                    list.addAll(data);
                    tContTypeList.clear();
                    tContTypeList.addAll(cdata);
                    tPageTypeList.clear();
                    tPageTypeList.addAll(pdata);
                    tSizeTypeList.clear();
                    tSizeTypeList.addAll(sdata);
                }
                else{
                    tOrderList.clear();
                    tOrderList.addAll(data);
                }
            } catch(InterruptedException ignore) { }
            return null;
        }
        @Override protected void finished() {
            setMessage("Done.");
            setSaveNeeded(false);
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ADagentApp.getApplication().getMainFrame();
            aboutBox = new ADagentAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ADagentApp.getApplication().show(aboutBox);
    }

    @Action
    public void editCustomerRecord() {
        
        Collection<adagent.TOrder> ts = tOrderList;
        adagent.TOrder t = new adagent.TOrder();

        t.setTEmployee(currentUser);
        tOrderList.add(t);
        int row = ts.size()-1;
        orderTable.setRowSelectionInterval(row, row);
        orderTable.scrollRectToVisible(orderTable.getCellRect(row, 0, true));
        
        
        int index = orderTable.getSelectedRow();
        adagent.TOrder T = tOrderList.get(orderTable.convertRowIndexToModel(index));

        if(doEditDialog(T) == 0) {
            entityManager.persist(t);
            setSaveNeeded(true);
        }
        else {
            orderTable.setRowSelectionInterval(row, row);
            tOrderList.remove(t);
        }
    }

    private int doEditDialog(adagent.TOrder T) {
        JFrame mainFrame = ADagentApp.getApplication().getMainFrame();
        CustomerOrder orderDialog = new CustomerOrder(mainFrame, false);
        orderDialog.setCurrentOrder(T);
        orderDialog.setVisible(true);

        java.util.Date odate = T.getAPostingDate();
        Short acont = T.getACont();
        Short apage = T.getAPage();
        Short asize = T.getASize();
        if(odate != null
                && acont != null && apage != null && asize != null
                && !T.getCName().equals("")) {
//            System.out.println(T.getCName().toString());
//            System.out.println(T.getACont().toString());
//            System.out.println(T.getAPage().toString());
//            System.out.println(T.getASize().toString());
//            System.out.println(T.getAPostingDate().toString());
            return 0;
        }

        return -1;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        loginMenuItem = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(adagent.ADagentApp.class).getContext().getResourceMap(ADagentView.class);
        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory(resourceMap.getString("entityManager.persistenceUnit")).createEntityManager(); // NOI18N
        query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("query.query")); // NOI18N
        list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(query.getResultList());
        frameTab = new javax.swing.JTabbedPane();
        empPane = new javax.swing.JPanel();
        displayEmployee = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        contTypeTableCellRenderer1 = new adagent.ContTypeTableCellRenderer();
        pageTypeTableCellRenderer1 = new adagent.PageTypeTableCellRenderer();
        sizeTypeTableCellRenderer1 = new adagent.SizeTypeTableCellRenderer();
        editCustomerButton = new javax.swing.JButton();
        subPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ContentCostTable = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        PageCostTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        SizeWeightTable = new javax.swing.JTable();
        mainPanel = new javax.swing.JPanel();
        masterScrollPane = new javax.swing.JScrollPane();
        masterTable = new javax.swing.JTable();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        detailScrollPane = new javax.swing.JScrollPane();
        detailTable = new javax.swing.JTable();
        deleteDetailButton = new javax.swing.JButton();
        newDetailButton = new javax.swing.JButton();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        saveButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        tOrderQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("tOrderQuery.query")); // NOI18N
        tOrderQuery.setParameter(1, 0);
        tOrderList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(tOrderQuery.getResultList());
        loginDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        LoginButton = new javax.swing.JButton();
        userText = new javax.swing.JTextField();
        passwdText = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        alertDialog = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        alertOK = new javax.swing.JButton();
        tContTypeQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("tContTypeQuery.query")); // NOI18N
        tContTypeList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(tContTypeQuery.getResultList());
        tPageTypeQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("tPageTypeQuery.query")); // NOI18N
        tPageTypeList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(tPageTypeQuery.getResultList());
        tSizeTypeQuery = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery(resourceMap.getString("tSizeTypeQuery.query")); // NOI18N
        tSizeTypeList = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : org.jdesktop.observablecollections.ObservableCollections.observableList(tSizeTypeQuery.getResultList());

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(adagent.ADagentApp.class).getContext().getActionMap(ADagentView.class, this);
        loginMenuItem.setAction(actionMap.get("loginUsers")); // NOI18N
        loginMenuItem.setText(resourceMap.getString("loginMenuItem.text")); // NOI18N
        loginMenuItem.setName("loginMenuItem"); // NOI18N
        fileMenu.add(loginMenuItem);

        logoutMenuItem.setAction(actionMap.get("logoutUsers")); // NOI18N
        logoutMenuItem.setText(resourceMap.getString("logoutMenuItem.text")); // NOI18N
        logoutMenuItem.setName("logoutMenuItem"); // NOI18N
        fileMenu.add(logoutMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        frameTab.setName("frameTab"); // NOI18N

        empPane.setName("empPane"); // NOI18N

        displayEmployee.setFont(resourceMap.getFont("displayEmployee.font")); // NOI18N
        displayEmployee.setText(resourceMap.getString("displayEmployee.text")); // NOI18N
        displayEmployee.setName("displayEmployee"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        orderTable.setBackground(resourceMap.getColor("orderTable.background")); // NOI18N
        orderTable.setName("orderTable"); // NOI18N

        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tOrderList, orderTable);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${CName}"));
        columnBinding.setColumnName("CName");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ACont}"));
        columnBinding.setColumnName("ACont");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${APage}"));
        columnBinding.setColumnName("APage");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ASize}"));
        columnBinding.setColumnName("ASize");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${APostingDate}"));
        columnBinding.setColumnName("APosting Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ABill}"));
        columnBinding.setColumnName("ABill");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        orderTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                orderTablePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(orderTable);
        orderTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        orderTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        orderTable.getColumnModel().getColumn(1).setCellRenderer(contTypeTableCellRenderer1);
        orderTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title4")); // NOI18N
        orderTable.getColumnModel().getColumn(2).setCellRenderer(pageTypeTableCellRenderer1);
        orderTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N
        orderTable.getColumnModel().getColumn(3).setCellRenderer(sizeTypeTableCellRenderer1);
        orderTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTable1.columnModel.title6")); // NOI18N
        orderTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("jTable1.columnModel.title5")); // NOI18N

        contTypeTableCellRenderer1.setText(resourceMap.getString("contTypeTableCellRenderer1.text")); // NOI18N
        contTypeTableCellRenderer1.setName("contTypeTableCellRenderer1"); // NOI18N

        pageTypeTableCellRenderer1.setName("pageTypeTableCellRenderer1"); // NOI18N

        sizeTypeTableCellRenderer1.setName("sizeTypeTableCellRenderer1"); // NOI18N

        editCustomerButton.setAction(actionMap.get("editCustomerRecord")); // NOI18N
        editCustomerButton.setText(resourceMap.getString("editCustomerButton.text")); // NOI18N
        editCustomerButton.setName("editCustomerButton"); // NOI18N

        javax.swing.GroupLayout empPaneLayout = new javax.swing.GroupLayout(empPane);
        empPane.setLayout(empPaneLayout);
        empPaneLayout.setHorizontalGroup(
            empPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(empPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(empPaneLayout.createSequentialGroup()
                        .addGroup(empPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
                            .addGroup(empPaneLayout.createSequentialGroup()
                                .addComponent(displayEmployee)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(contTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pageTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sizeTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, empPaneLayout.createSequentialGroup()
                        .addComponent(editCustomerButton)
                        .addGap(11, 11, 11))))
        );
        empPaneLayout.setVerticalGroup(
            empPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(empPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(empPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(displayEmployee)
                    .addComponent(contTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pageTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizeTypeTableCellRenderer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 229, Short.MAX_VALUE)
                .addComponent(editCustomerButton)
                .addContainerGap())
        );

        frameTab.addTab(resourceMap.getString("empPane.TabConstraints.tabTitle"), empPane); // NOI18N

        subPanel.setName("subPanel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        ContentCostTable.setName("ContentCostTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tContTypeList, ContentCostTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${contentName}"));
        columnBinding.setColumnName("Content Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${contentCost}"));
        columnBinding.setColumnName("Content Cost");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(ContentCostTable);
        ContentCostTable.getColumnModel().getColumn(0).setResizable(false);
        ContentCostTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("ContentCostTable.columnModel.title1")); // NOI18N
        ContentCostTable.getColumnModel().getColumn(1).setResizable(false);
        ContentCostTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("ContentCostTable.columnModel.title0")); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        PageCostTable.setName("PageCostTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tPageTypeList, PageCostTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${pageName}"));
        columnBinding.setColumnName("Page Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${pageCost}"));
        columnBinding.setColumnName("Page Cost");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(PageCostTable);
        PageCostTable.getColumnModel().getColumn(0).setResizable(false);
        PageCostTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("PageCostTable.columnModel.title1")); // NOI18N
        PageCostTable.getColumnModel().getColumn(1).setResizable(false);
        PageCostTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("PageCostTable.columnModel.title0")); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        SizeWeightTable.setName("SizeWeightTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, tSizeTypeList, SizeWeightTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sizeName}"));
        columnBinding.setColumnName("Size Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sizeWeight}"));
        columnBinding.setColumnName("Size Weight");
        columnBinding.setColumnClass(Double.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane4.setViewportView(SizeWeightTable);
        SizeWeightTable.getColumnModel().getColumn(0).setResizable(false);
        SizeWeightTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("SizeWeightTable.columnModel.title0")); // NOI18N
        SizeWeightTable.getColumnModel().getColumn(1).setResizable(false);
        SizeWeightTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("SizeWeightTable.columnModel.title1")); // NOI18N

        javax.swing.GroupLayout subPanelLayout = new javax.swing.GroupLayout(subPanel);
        subPanel.setLayout(subPanelLayout);
        subPanelLayout.setHorizontalGroup(
            subPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        subPanelLayout.setVerticalGroup(
            subPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        frameTab.addTab(resourceMap.getString("subPanel.TabConstraints.tabTitle"), subPanel); // NOI18N

        mainPanel.setName("mainPanel"); // NOI18N

        masterScrollPane.setName("masterScrollPane"); // NOI18N

        masterTable.setName("masterTable"); // NOI18N

        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, list, masterTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${EName}"));
        columnBinding.setColumnName("EName");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${EPassword}"));
        columnBinding.setColumnName("EPassword");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        masterScrollPane.setViewportView(masterTable);
        masterTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("masterTable.columnModel.title0")); // NOI18N
        masterTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("masterTable.columnModel.title1")); // NOI18N

        newButton.setAction(actionMap.get("newRecord")); // NOI18N
        newButton.setName("newButton"); // NOI18N

        deleteButton.setAction(actionMap.get("deleteRecord")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        detailScrollPane.setName("detailScrollPane"); // NOI18N

        detailTable.setBackground(resourceMap.getColor("detailTable.background")); // NOI18N
        detailTable.setName("detailTable"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${selectedElement.TOrderList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, eLProperty, detailTable);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${CName}"));
        columnBinding.setColumnName("CName");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ACont}"));
        columnBinding.setColumnName("ACont");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${APage}"));
        columnBinding.setColumnName("APage");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ASize}"));
        columnBinding.setColumnName("ASize");
        columnBinding.setColumnClass(Short.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${APostingDate}"));
        columnBinding.setColumnName("APosting Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${ABill}"));
        columnBinding.setColumnName("ABill");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        jTableBinding.setSourceUnreadableValue(java.util.Collections.emptyList());
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        detailScrollPane.setViewportView(detailTable);
        detailTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("detailTable.columnModel.title0")); // NOI18N
        detailTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("detailTable.columnModel.title1")); // NOI18N
        detailTable.getColumnModel().getColumn(1).setCellRenderer(contTypeTableCellRenderer1);
        detailTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("detailTable.columnModel.title3")); // NOI18N
        detailTable.getColumnModel().getColumn(2).setCellRenderer(pageTypeTableCellRenderer1);
        detailTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("detailTable.columnModel.title2")); // NOI18N
        detailTable.getColumnModel().getColumn(3).setCellRenderer(sizeTypeTableCellRenderer1);
        detailTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("detailTable.columnModel.title5")); // NOI18N
        detailTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("detailTable.columnModel.title4")); // NOI18N

        deleteDetailButton.setAction(actionMap.get("deleteDetailRecord")); // NOI18N
        deleteDetailButton.setName("deleteDetailButton"); // NOI18N

        newDetailButton.setAction(actionMap.get("editDetailRecord")); // NOI18N
        newDetailButton.setText(resourceMap.getString("newDetailButton.text")); // NOI18N
        newDetailButton.setName("newDetailButton"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(masterScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(newButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(newDetailButton)
                        .addGap(18, 18, 18)
                        .addComponent(deleteDetailButton))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(detailScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)))
                .addContainerGap())
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteButton, newButton});

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {deleteDetailButton, newDetailButton});

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(masterScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton)
                    .addComponent(newButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteDetailButton)
                    .addComponent(newDetailButton))
                .addContainerGap())
        );

        frameTab.addTab(resourceMap.getString("mainPanel.TabConstraints.tabTitle"), mainPanel); // NOI18N

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(442, 100));

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        saveButton.setAction(actionMap.get("save")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        refreshButton.setAction(actionMap.get("refresh")); // NOI18N
        refreshButton.setName("refreshButton"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(statusPanelLayout.createSequentialGroup()
                            .addComponent(statusMessageLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 326, Short.MAX_VALUE)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(statusAnimationLabel)
                            .addContainerGap())
                        .addComponent(statusPanelSeparator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                        .addComponent(refreshButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveButton)
                        .addContainerGap())))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(refreshButton))
                .addGap(18, 18, 18)
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );

        loginDialog.setTitle(resourceMap.getString("loginDialog.title")); // NOI18N
        loginDialog.setBounds(new java.awt.Rectangle(0, 0, 300, 180));
        loginDialog.setModal(true);
        loginDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        loginDialog.setName("loginDialog"); // NOI18N
        loginDialog.setResizable(false);
        loginDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                loginDialogWindowClosing(evt);
            }
        });

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        LoginButton.setText(resourceMap.getString("LoginButton.text")); // NOI18N
        LoginButton.setName("LoginButton"); // NOI18N
        LoginButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginButtonActionPerformed(evt);
            }
        });

        userText.setName("userText"); // NOI18N

        passwdText.setName("passwdText"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        CancelButton.setText(resourceMap.getString("CancelButton.text")); // NOI18N
        CancelButton.setName("CancelButton"); // NOI18N
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout loginDialogLayout = new javax.swing.GroupLayout(loginDialog.getContentPane());
        loginDialog.getContentPane().setLayout(loginDialogLayout);
        loginDialogLayout.setHorizontalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginDialogLayout.createSequentialGroup()
                        .addComponent(LoginButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CancelButton))
                    .addComponent(passwdText, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                    .addComponent(userText, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap())
        );
        loginDialogLayout.setVerticalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(userText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelButton)
                    .addComponent(LoginButton))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        alertDialog.setTitle(resourceMap.getString("alertDialog.title")); // NOI18N
        alertDialog.setBounds(new java.awt.Rectangle(0, 0, 320, 180));
        alertDialog.setIconImage(null);
        alertDialog.setIconImages(null);
        alertDialog.setModal(true);
        alertDialog.setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        alertDialog.setName("alertDialog"); // NOI18N
        alertDialog.setResizable(false);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        alertOK.setText(resourceMap.getString("alertOK.text")); // NOI18N
        alertOK.setName("alertOK"); // NOI18N
        alertOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alertOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout alertDialogLayout = new javax.swing.GroupLayout(alertDialog.getContentPane());
        alertDialog.getContentPane().setLayout(alertDialogLayout);
        alertDialogLayout.setHorizontalGroup(
            alertDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alertDialogLayout.createSequentialGroup()
                .addGroup(alertDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(alertDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3))
                    .addGroup(alertDialogLayout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(alertOK)))
                .addContainerGap(101, Short.MAX_VALUE))
        );
        alertDialogLayout.setVerticalGroup(
            alertDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(alertDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(alertOK)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        setComponent(frameTab);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void LoginButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginButtonActionPerformed
        if(adminUser.equals(userText.getText()) &&
                adminPasswd.equals(passwdText.getText())){
            dologinAdminMode();
            dologinSuccess();
            return;
        } else {
            for(adagent.TEmployee emp : list){
                if(emp.getEName().equals(userText.getText()) &&
                        emp.getEPassword().equals(passwdText.getText())){
                    dologinUserMode(emp);
                    dologinSuccess();
                    return;
                }
            }
        }
        alertDialog.show();
        return;
}//GEN-LAST:event_LoginButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        // TODO add your handling code here:
        System.exit(0);
}//GEN-LAST:event_CancelButtonActionPerformed

    private void loginDialogWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_loginDialogWindowClosing
        // TODO add your handling code here:
        System.exit(0);
}//GEN-LAST:event_loginDialogWindowClosing

    private void alertOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alertOKActionPerformed
        // TODO add your handling code here:
        alertDialog.hide();
}//GEN-LAST:event_alertOKActionPerformed

    private void orderTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_orderTablePropertyChange
        System.out.println("proertyChange");
        // TODO add your handling code here:
    }//GEN-LAST:event_orderTablePropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JTable ContentCostTable;
    private javax.swing.JButton LoginButton;
    private javax.swing.JTable PageCostTable;
    private javax.swing.JTable SizeWeightTable;
    private javax.swing.JDialog alertDialog;
    private javax.swing.JButton alertOK;
    private adagent.ContTypeTableCellRenderer contTypeTableCellRenderer1;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteDetailButton;
    private javax.swing.JScrollPane detailScrollPane;
    private javax.swing.JTable detailTable;
    private javax.swing.JLabel displayEmployee;
    private javax.swing.JButton editCustomerButton;
    private javax.swing.JPanel empPane;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JTabbedPane frameTab;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private java.util.List<adagent.TEmployee> list;
    private javax.swing.JDialog loginDialog;
    private javax.swing.JMenuItem loginMenuItem;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane masterScrollPane;
    private javax.swing.JTable masterTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newDetailButton;
    private javax.swing.JTable orderTable;
    private adagent.PageTypeTableCellRenderer pageTypeTableCellRenderer1;
    private javax.swing.JPasswordField passwdText;
    private javax.swing.JProgressBar progressBar;
    private javax.persistence.Query query;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton saveButton;
    private adagent.SizeTypeTableCellRenderer sizeTypeTableCellRenderer1;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel subPanel;
    private java.util.List<adagent.TContType> tContTypeList;
    private javax.persistence.Query tContTypeQuery;
    private java.util.List<adagent.TOrder> tOrderList;
    private javax.persistence.Query tOrderQuery;
    private java.util.List<adagent.TPageType> tPageTypeList;
    private javax.persistence.Query tPageTypeQuery;
    private java.util.List<adagent.TSizeType> tSizeTypeList;
    private javax.persistence.Query tSizeTypeQuery;
    private javax.swing.JTextField userText;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private adagent.TEmployee currentUser;
    private static final String adminUser= "admin";
    private static final String adminPasswd= "123";
    private boolean isAdminMode;

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private boolean saveNeeded;

    private void initComponents2() {
        frameTab.setVisible(false);
        frameTab.setEnabledAt(0, false);
        frameTab.setEnabledAt(1, false);
        frameTab.setEnabledAt(2, false);
        logoutMenuItem.setEnabled(false);
    }

    private void dologinSuccess() {
        userText.setText("");
        passwdText.setText("");
        loginMenuItem.setEnabled(false);
        logoutMenuItem.setEnabled(true);
        frameTab.setVisible(true);
        this.refreshButton.doClick();
    }
  
    private void dologinAdminMode() {
        isAdminMode = true;
        System.out.println("Admin Mode");
        loginDialog.hide();
        frameTab.setSelectedIndex(2);
        frameTab.setEnabledAt(1, true);
        frameTab.setEnabledAt(2, true);
    }

    private void dologinUserMode(TEmployee emp) {
       isAdminMode = false;
        currentUser = emp;
        loginDialog.hide();
        System.out.println("Employee Mode:" + currentUser.getEName());
        displayEmployee.setText(currentUser.getEName());
        frameTab.setSelectedIndex(0);
        frameTab.setEnabledAt(0, true);
    }

    @Action
    public void loginUsers() {
        loginDialog.setVisible(true);
    }

    @Action
    public void logoutUsers() {
        initComponents2();
        currentUser = null;
        loginMenuItem.setEnabled(true);
    }
}
