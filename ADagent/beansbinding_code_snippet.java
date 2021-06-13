
		// In case the source data in a database is datetime, destitination data is string for textfield. This is example of a conversion.

		//postingDateField.setName("postingDateField"); // NOI18N
		//binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, masterTable, org.jdesktop.beansbinding.ELProperty.create("${selectedElement.postingDate}"), postingDateField, org.jdesktop.beansbinding.BeanProperty.create("text"));
    	binding.setConverter(new org.jdesktop.beansbinding.Converter<java.util.Date, String>() {
		   @Override
		   public String convertForward(java.util.Date value) {
		      return java.text.DateFormat.getDateInstance().format(value);
		   }

		   @Override
		   public java.util.Date convertReverse(String value) {

		      try {
		         return java.text.DateFormat.getDateInstance().parse(value);
		      } catch (java.text.ParseException e) {
		         return java.util.Calendar.getInstance().getTime();
		      }
		   }
		});
