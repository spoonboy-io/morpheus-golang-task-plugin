package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.*

/**
 * Example AbstractTaskService. Each method demonstrates building an example TaskConfig for the relevant task type
 */
class GolangTaskService extends AbstractTaskService {
	MorpheusContext context

	GolangTaskService(MorpheusContext context) {
		this.context = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return context
	}

	@Override
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildLocalTaskConfig([:], task, [], opts).blockingGet()
		if(instance) {
			config = buildInstanceTaskConfig(instance, [:], task, [], opts).blockingGet()
		}
		if(container) {
			config = buildContainerTaskConfig(container, [:], task, [], opts).blockingGet()
		}
	
		executeTask(task, config)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		TaskConfig config = buildComputeServerTaskConfig(server, [:], task, [], opts).blockingGet()
		context.executeCommandOnServer(server, 'echo $JAVA_HOME')
		context.executeCommandOnServer(server, 'echo $JAVA_HOME', false, 'user', 'password', null, null, null, false, false)
		executeTask(task, config)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		TaskConfig config = buildComputeServerTaskConfig(server, [:], task, [], [:]).blockingGet()
		context.executeCommandOnServer(server, 'echo $JAVA_HOME')
		executeTask(task, config)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		TaskConfig config = buildContainerTaskConfig(container, [:], task, [], opts).blockingGet()
		context.executeCommandOnWorkload(container, 'echo $JAVA_HOME')
		context.executeCommandOnWorkload(container, 'echo $JAVA_HOME', 'user', 'password', null, null, null, false, null, false)
		executeTask(task, config)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		TaskConfig config = buildContainerTaskConfig(container, [:], task, [], [:]).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildRemoteTaskConfig([:], task, [], opts).blockingGet()
		context.executeCommandOnWorkload(container, 'echo $JAVA_HOME')
		executeTask(task, config)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildRemoteTaskConfig([:], task, [], [:]).blockingGet()
		context.executeSshCommand('localhost', 8080, 'bob', 'password', 'echo $JAVA_HOME', null, null, null, false, null, LogLevel.debug, false, null, true)
		executeTask(task, config)
	}

	TaskResult executeTask(Task task, TaskConfig config) {
		def taskOption = task.taskOptions.find { it.optionType.code == 'golangTaskScript' }
		String data = taskOption.value
		
		// create the file
		Date ts = new Date();
		String fName = "/tmp/morpheus-task-${ts.getTime()}.go"
		File container = new File (fName)

		container << data

		// run it
		def sout = new StringBuilder()
		def serr = new StringBuilder()
		def proc = "go run ${fName}".execute()
		proc.consumeProcessOutput(sout, serr)
		proc.waitForOrKill(1000)

		// clean up
		boolean deleted =  container.delete()  

		new TaskResult(
				success: true,
				data   : sout,
				output : sout,
				error : serr
		)
	}
}
