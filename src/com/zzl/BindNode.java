package com.zzl;

import com.bppleman.processmanagement.process.ProcessSimulator;

public class BindNode {

	private ProcessSimulator process;
	private MemNode memNode;
	public BindNode(ProcessSimulator process,MemNode memNode){
		this.process=process;
		this.memNode=memNode;
	}
	public ProcessSimulator getProcess() {
		return process;
	}
	public void setProcess(ProcessSimulator process) {
		this.process = process;
	}
	public MemNode getMemNode() {
		return memNode;
	}
	public void setMemNode(MemNode memNode) {
		this.memNode = memNode;
	}
	
}
