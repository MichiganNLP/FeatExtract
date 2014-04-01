package featpruners.post;

public abstract class PostFeaturePruner {

	protected Integer N;
	protected Double threshold;
	
	public PostFeaturePruner(Integer N){
		this.N = N;
	}
	
	public PostFeaturePruner(Double threshold){
		this.threshold = threshold;
	}
	
	public abstract void prune(String intputFile, String outputFile);
}
