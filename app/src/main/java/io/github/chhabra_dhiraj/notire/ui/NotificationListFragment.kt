package io.github.chhabra_dhiraj.notire.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import io.github.chhabra_dhiraj.notire.domain.Notification
import io.github.chhabra_dhiraj.notire.ui.adapters.NotificationsListAdapter
import io.github.chhabra_dhiraj.notire.viewmodels.NotificationsViewModel
import io.github.chhabra_dhiraj.spaceflightnews.databinding.FragmentNotificationListBinding
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class NotificationListFragment : Fragment() {

    private var _binding: FragmentNotificationListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val notificationsViewModel: NotificationsViewModel by viewModels()

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

        notificationsViewModel.notifications.observe(viewLifecycleOwner) {
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
        }
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
