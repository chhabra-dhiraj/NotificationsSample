package io.github.chhabra_dhiraj.notire.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import io.github.chhabra_dhiraj.notire.R
import io.github.chhabra_dhiraj.notire.databinding.FragmentNotificationListBinding
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.ui.adapters.NotificationsListAdapter
import io.github.chhabra_dhiraj.notire.viewmodels.NotificationsViewModel
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val notificationsViewModel: NotificationsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, NotificationsViewModel.Factory(activity.application))
            .get(NotificationsViewModel::class.java)
    }

    private val notifications = ArrayList<Notification>()
    private val pinnedNotifications = ArrayList<Notification>()
    private val notificationsAdapter = NotificationsListAdapter()
    private val pinnedNotificationsAdapter = NotificationsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationListBinding.inflate(inflater, container, false)
        binding.notificationsRecyclerView.adapter = notificationsAdapter
        binding.pinnedNotificationsRecyclerView.adapter = pinnedNotificationsAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnUpdate.setOnClickListener {
            binding.progressBarNotifications.visibility = View.VISIBLE
            binding.dataLayout.visibility = View.GONE
            binding.tvNoNotifications.visibility = View.GONE
            binding.btnUpdate.isEnabled = false
            notificationsViewModel.refreshNotifications(requireActivity().application)
        }

        notificationsViewModel.isSuccess.observe(viewLifecycleOwner, Observer {
            if (!it) {
                binding.btnUpdate.isEnabled = true
                binding.progressBarNotifications.visibility = View.GONE
                Snackbar.make(
                    binding.root,
                    "could not refresh notifications",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })

        notificationsViewModel.notifications.observe(viewLifecycleOwner, Observer {
            it?.apply {
                if (it.isEmpty()) {
                    binding.btnUpdate.isEnabled = true
                    binding.btnUpdate.visibility = View.VISIBLE
                    binding.progressBarNotifications.visibility = View.GONE
                    binding.tvNoNotifications.visibility = View.VISIBLE
                    binding.progressBarNotifications.visibility = View.GONE
                } else {
                    notifications.clear()
                    pinnedNotifications.clear()
                    divideNotifications(it)
                    notificationsAdapter.submitList(notifications)
                    binding.btnUpdate.isEnabled = true
                    binding.btnUpdate.visibility = View.VISIBLE
                    binding.progressBarNotifications.visibility = View.GONE
                    binding.tvNoNotifications.visibility = View.GONE
                    binding.dataLayout.visibility = View.VISIBLE
                    if (pinnedNotifications.isNotEmpty()) {
                        pinnedNotificationsAdapter.submitList(pinnedNotifications)
                        binding.pinnedNotificationsRecyclerView.visibility = View.VISIBLE
                        binding.labelPinnedNotifications.visibility = View.VISIBLE
                    } else {
                        binding.pinnedNotificationsRecyclerView.visibility = View.GONE
                        binding.labelPinnedNotifications.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun divideNotifications(notifications: List<Notification>) {
        notifications.map {
            if (it.pinned) {
                pinnedNotifications.add(it)
            } else {
                this.notifications.add(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
