package com.example.snapshotsforreddit.ui.general.redditpage

/*
@AndroidEntryPoint
class FrontPageFragment : Fragment() {
    private lateinit var tokensDatastore: TokensDatastore
    private val viewModel: FrontPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialize tokensDatastore
        tokensDatastore = TokensDatastore(requireContext())



        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }


        val binding = FragmentFrontPageBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.postsList.adapter = FrontPageAdapter(FrontPageListener {
            val action = FrontPageFragmentDirections.actionFrontPageFragmentToPostDetailFragment(
                //just pass the object over
               it!!
            )
            this.findNavController().navigate(action,options)
            //findNavController().navigate(R.id.action_frontPageFragment_to_postDetailFragment)

        })

        //viewModel.getPosts()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

    }
}

 */