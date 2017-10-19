package view;

/**
 * Created by daichi on 10/19/17.
 */
public interface IView<ViewModelType> {
    void bind(ViewModelType viewModel);
}
