package com.morpheusdata.task

import com.morpheusdata.core.Plugin
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.ViewModel

class GolangTaskPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-golang-task-plugin'
	}

	@Override
	void initialize() {
		GolangTaskProvider golangTaskProvider = new GolangTaskProvider(this, morpheus)
		this.setName("Golang Task Plugin")
		this.setDescription("Provides a Golang script execution task in Morpheus")
		this.setAuthor("Ollie Phillips")
		this.pluginProviders.put(golangTaskProvider.code, golangTaskProvider)
	}

	@Override
	void onDestroy() {
		morpheus.task.disableTask('golangTask')
	}

}