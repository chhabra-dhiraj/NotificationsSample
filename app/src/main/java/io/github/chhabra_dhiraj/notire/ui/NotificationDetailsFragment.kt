package io.github.chhabra_dhiraj.notire.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import io.github.chhabra_dhiraj.notire.databinding.FragmentNotificationDetailsBinding
import io.github.chhabra_dhiraj.notire.viewmodels.NotificationDetailsViewModel
import io.github.chhabra_dhiraj.notire.viewmodels.NotificationsViewModel


/**
 * A simple [Fragment] subclass.
 */
class NotificationDetailsFragment : Fragment() {

    private var _binding: FragmentNotificationDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args: NotificationDetailsFragmentArgs by navArgs()

    private val notificationsDetailsViewModel: NotificationDetailsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, NotificationDetailsViewModel.Factory(activity.application))
            .get(NotificationDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        _binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvTitle.text = args.notification.title
        binding.tvSource.text = args.notification.source
        binding.tvMessage.text = args.notification.message

        binding.btnOpenLink.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(args.notification.links))
            startActivity(browserIntent)
        }

        binding.btnPin.setOnClickListener {
            args.notification.pinned = true
            notificationsDetailsViewModel.pinNotification(args.notification)
        }

        notificationsDetailsViewModel.isPinSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(binding.root, "Notification Pinned", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, "Issue Pinning Notification", Snackbar.LENGTH_SHORT)
                    .show()
            }
        })

        binding.btnDismiss.setOnClickListener {
            notificationsDetailsViewModel.deleteNotification(args.notification)
        }

        notificationsDetailsViewModel.isDeleteSuccess.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
            } else {
                Snackbar.make(binding.root, "Issue Dismissing notification", Snackbar.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
