import java.util.Set;

import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;
import sun.tools.jps.Arguments;

public class MyJps {

	public static void main(String[] args) throws Exception {
		Arguments arg = new Arguments(args);
		HostIdentifier hostid = arg.hostId();
//		System.out.println(hostid);
		MonitoredHost monitorHost = MonitoredHost.getMonitoredHost(hostid);
//		System.out.println(monitorHost);
		Set<Integer> jvms = monitorHost.activeVms();
		
		for (Integer jvm : jvms) {
			int jvmid = jvm.intValue();
			
			String vmidString = "//" + jvmid + "?mode=r";
			
			VmIdentifier id = new VmIdentifier(vmidString);
			MonitoredVm vm = monitorHost.getMonitoredVm(id, 0);
			
			StringBuffer output = new StringBuffer();
			
			output.append(jvmid+":");
			
			output.append(" mainClass:" + MonitoredVmUtil.mainClass(vm, arg.showLongPaths()));
			if (arg.showMainArgs()) {
				output.append(" mainArgs:" + MonitoredVmUtil.mainArgs(vm));
				output.append(" jvmArgs:" + MonitoredVmUtil.jvmArgs(vm));
			}
			if (arg.showVmFlags()) {
				output.append(" jvmFlags:" + MonitoredVmUtil.jvmFlags(vm));
			}
			System.out.println(output);
			monitorHost.detach(vm);
		}
		
	}

}
